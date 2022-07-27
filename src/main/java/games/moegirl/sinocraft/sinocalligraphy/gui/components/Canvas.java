package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawVersions;
import games.moegirl.sinocraft.sinocalligraphy.drawing.SmallBlackWhiteBrushHolder;
import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import games.moegirl.sinocraft.sinocore.api.utility.GLSwitcher;
import games.moegirl.sinocraft.sinocore.api.utility.texture.TextureMap;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import static games.moegirl.sinocraft.sinocalligraphy.drawing.SmallBlackWhiteBrushHolder.SIZE;

@OnlyIn(Dist.CLIENT)
public class Canvas extends AbstractWidget {

    private final TextureMap atlas;
    private final String canvas, shadow;
    private final IntSupplier getColor;
    private final IntConsumer setColor;
    private final AbstractContainerScreen<?> parent;
    private int canvasSize;

    private DrawHolder draw = null;

    private boolean isEnable = false;
    private boolean isDrag = false;
    private int dragButton = 0;

    private boolean altPressed = false;

    public Canvas(AbstractContainerScreen<?> parent, TextureMap atlas, String canvas,
                  String shadow, IntSupplier getColor, IntConsumer setColor, XuanPaperType type) {
        super(0, 0, 0, 0, TextComponent.EMPTY);
        this.atlas = atlas;
        this.parent = parent;
        this.canvas = canvas;
        this.shadow = shadow;
        this.getColor = getColor;
        this.setColor = setColor;

        draw = DrawVersions.LATEST_BRUSH_VERSION.newDraw(type);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (isMouseOver(pMouseX, pMouseY)) {
            isDrag = true;
            dragButton = pButton;
        }
        return false;
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        if (isMouseOver(pMouseX, pMouseY) && isDrag) {
            if (isValidClickButton(dragButton)) {
                drawPoint(pMouseX, pMouseY);
            } else {
                if (altPressed) {
                    takeColor(pMouseX, pMouseY);
                } else {
                    removeColor(pMouseX, pMouseY);
                }
            }
        }
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        isDrag = false;
        return false;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (isMouseOver(pMouseX, pMouseY)) {
            if (isValidClickButton(pButton)) {
                drawPoint(pMouseX, pMouseY);
            } else {
                if (altPressed) {
                    takeColor(pMouseX, pMouseY);
                } else {
                    removeColor(pMouseX, pMouseY);
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_ALT || keyCode == GLFW.GLFW_KEY_RIGHT_ALT) {
            altPressed = true;
        }

        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_ALT || keyCode == GLFW.GLFW_KEY_RIGHT_ALT) {
            altPressed = false;
        }

        return true;
    }

    @Override
    public void render(PoseStack stack, int pMouseX, int pMouseY, float pPartialTick) {
        atlas.blitTexture(stack, canvas, parent);
        draw.render().draw(stack, x + 1, y + 1, canvasSize, canvasSize);
        if (!isEnable()) {
            atlas.blitTexture(stack, shadow, parent, GLSwitcher.blend().enable());
        }
    }

    private int getPointIndex(double pMouseX, double pMouseY) {
        int cellSize = canvasSize / SIZE;
        int x = Mth.clamp((int) ((Math.round(pMouseX) - this.x) / cellSize), 0, SIZE - 1);
        int y = Mth.clamp((int) ((Math.round(pMouseY) - this.y) / cellSize), 0, SIZE - 1);
        return x * SIZE + y;
    }

    private void drawPoint(double pMouseX, double pMouseY) {
        if (isEnable) {
            ((byte[]) draw.getData())[getPointIndex(pMouseX, pMouseY)] = (byte) getColor.getAsInt();
        }
    }

    private void removeColor(double pMouseX, double pMouseY) {
        if (isEnable) {
            ((byte[]) draw.getData())[getPointIndex(pMouseX, pMouseY)] = (byte) 0;
        }
    }

    private void takeColor(double mouseX, double mouseY) {
        if (isEnable && altPressed) {
            setColor.accept(((byte[]) draw.getData())[getPointIndex(mouseX, mouseY)]);
            if (parent instanceof BrushGuiScreen gui) {
                var list = gui.getColorSelection();
                list.setSelectedItem(list.getEntry(getColor.getAsInt()));
            }
        }
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public DrawHolder getDraw(@Nullable Player author) {
        if (draw.hasAuthor()) {
            return draw;
        }

        draw.setAuthor(author);
        return draw;
    }

    public boolean setDraw(DrawHolder holder) {
        if (holder instanceof SmallBlackWhiteBrushHolder) {
            DrawHolder.copyDirectly(holder, draw);
            return true;
        }
        return false;
    }

    public Canvas resize(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = size;
        this.canvasSize = size - 2;
        return this;
    }

    public void setDrawType(XuanPaperType type) {
        draw.setType(type);
    }

    public XuanPaperType getDrawType() {
        return draw.getType();
    }

    public void clear() {
        draw = draw.getVersion().newDraw();
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, new TextComponent("Canvas"));
    }

    public boolean isEmpty() {
        return draw.isEmpty();
    }
}
