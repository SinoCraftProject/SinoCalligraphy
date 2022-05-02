package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.client.TextureAtlas;
import games.moegirl.sinocraft.sinocalligraphy.gui.GLSwitcher;
import games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import static games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem.PIXEL_COUNT;
import static games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem.SIZE;

@OnlyIn(Dist.CLIENT)
public class Canvas extends AbstractWidget {

    private final TextureAtlas atlas;
    private final String name;
    private final IntSupplier getColor;
    private final IntConsumer setColor;
    private int canvasSize;

    private byte[] draw = new byte[PIXEL_COUNT];
    private boolean isEnable = false;
    private boolean isDrag = false;
    private int dragButton = 0;

    public Canvas(TextureAtlas atlas, String name, IntSupplier getColor, IntConsumer setColor) {
        super(0, 0, 0, 0, TextComponent.EMPTY);
        this.atlas = atlas;
        this.name = name;
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
        atlas.blit(pPoseStack, name, x, y, width, height);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        int unit = canvasSize / SIZE;
        int x1 = x + 1;
        int x2 = x1 + unit;
        for (int i = 0; i < SIZE; i++) {
            int y1 = y + 1;
            int y2 = y1 + unit;
            for (int j = 0; j < SIZE; j++) {
                float color = 0.0625f * (16 - draw[i * SIZE + j]); // qyl27: For calculating grayscale.
                RenderSystem.setShaderColor(color, color, color, 1.0f);
                fill(pPoseStack, x1, y1, x2, y2, new Color(color, color, color, 1).getRGB());
                y1 = y2;
                y2 += unit;
            }
            x1 = x2;
            x2 += unit;
        }
        RenderSystem.enableTexture();

        if (!isEnable()) {
            atlas.blit(pPoseStack, name + "_disable", x, y, width, height, GLSwitcher.blend().enable());
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
            draw[getPointIndex(pMouseX, pMouseY)] = (byte) getColor.getAsInt();
        }
    }

    private void takeColor(double pMouseX, double pMouseY) {
        if (isEnable) {
            setColor.accept(draw[getPointIndex(pMouseX, pMouseY)]);
        }
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public byte[] getDraw() {
        return draw;
    }

    public void setDraw(byte[] draw) {
        this.draw = XuanPaperItem.adjustSize(draw);
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
        setDraw(new byte[PIXEL_COUNT]);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, new TextComponent("Canvas"));
    }
}
