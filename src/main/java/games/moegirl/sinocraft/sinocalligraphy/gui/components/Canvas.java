package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawVersions;
import games.moegirl.sinocraft.sinocalligraphy.drawing.SmallBlackWhiteBrushHolder;
import games.moegirl.sinocraft.sinocalligraphy.gui.texture.TextureMap;
import games.moegirl.sinocraft.sinocore.api.utility.GLSwitcher;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

    private DrawHolder draw = DrawVersions.LATEST_BRUSH_VERSION.newDraw();
    private boolean isEnable = false;
    private boolean isDrag = false;
    private int dragButton = 0;

    public Canvas(AbstractContainerScreen<?> parent, TextureMap atlas, String canvas, String shadow, IntSupplier getColor, IntConsumer setColor) {
        super(0, 0, 0, 0, TextComponent.EMPTY);
        this.atlas = atlas;
        this.parent = parent;
        this.canvas = canvas;
        this.shadow = shadow;
        this.getColor = getColor;
        this.setColor = setColor;
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
                takeColor(pMouseX, pMouseY);
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
                takeColor(pMouseX, pMouseY);
            }
        }
        return false;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        atlas.blitTexture(pPoseStack, canvas, parent);
        draw.render().draw(pPoseStack, x + 1, y + 1, canvasSize, canvasSize);
        if (!isEnable()) {
            atlas.blitTexture(pPoseStack, shadow, parent, GLSwitcher.blend().enable());
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
            ((byte[]) draw.data())[getPointIndex(pMouseX, pMouseY)] = (byte) getColor.getAsInt();
        }
    }

    private void takeColor(double pMouseX, double pMouseY) {
        if (isEnable) {
            setColor.accept(((byte[]) draw.data())[getPointIndex(pMouseX, pMouseY)]);
        }
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public DrawHolder getDraw(@Nullable Player author) {
        draw.setAuthor(author);
        return draw;
    }

    public boolean setDraw(DrawHolder holder) {
        if (holder instanceof SmallBlackWhiteBrushHolder) {
            DrawHolder.copyDirectly(holder, draw);
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

    public void clear() {
        draw = draw.version().newDraw();
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, new TextComponent("Canvas"));
    }
}
