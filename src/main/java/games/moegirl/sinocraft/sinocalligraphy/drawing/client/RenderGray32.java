package games.moegirl.sinocraft.sinocalligraphy.drawing.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import games.moegirl.sinocraft.sinocalligraphy.drawing.holder.HolderByte32;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FastColor;

import static games.moegirl.sinocraft.sinocalligraphy.drawing.client.RenderTypes.COLOR_256;
import static games.moegirl.sinocraft.sinocalligraphy.drawing.client.RenderTypes.COLOR_LIGHT_256;
import static games.moegirl.sinocraft.sinocalligraphy.drawing.holder.HolderByte32.SIZE;

public class RenderGray32 implements DrawRender {

    private final HolderByte32 holder;

    public RenderGray32(HolderByte32 holder) {
        this.holder = holder;
    }

    @Override
    public void draw(PoseStack pPoseStack, int x, int y, int width, int height) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        if (holder.isEmpty()) {
            GuiComponent.fill(pPoseStack, x, y, x + width, y + height, FastColor.ARGB32.color(255, 255, 255, 255));
        } else {
            int unitX = width / SIZE;
            int unitY = height / SIZE;
            int x1 = x;
            int x2 = x1 + unitX;
            byte[] draw = (byte[]) holder.getData();
            int index = 0;
            for (int i = 0; i < SIZE; i++) {
                int y1 = y;
                int y2 = y1 + unitY;
                for (int j = 0; j < SIZE; j++) {
                    int c = 16 * (16 - draw[index]) - 1;
                    float color = 0.0625f * (16 - draw[index++]); // qyl27: For calculating grayscale.
                    RenderSystem.setShaderColor(color, color, color, 1.0f);
                    GuiComponent.fill(pPoseStack, x1, y1, x2, y2, FastColor.ARGB32.color(255, c, c, c));
                    y1 = y2;
                    y2 += unitY;
                }
                x1 = x2;
                x2 += unitX;
            }
        }
        RenderSystem.enableTexture();
    }

    @Override
    public void draw(PoseStack stack, MultiBufferSource buffer, int light) {
        VertexConsumer vertex = buffer.getBuffer(COLOR_LIGHT_256);
        if (holder.isEmpty()) {
            vertex.vertex(stack.last().pose(), 0, 0, 0).color(1, 1, 1, 1).uv2(light).endVertex();
            vertex.vertex(stack.last().pose(), 0, SIZE, 0).color(1, 1, 1, 1).uv2(light).endVertex();
            vertex.vertex(stack.last().pose(), SIZE, SIZE, 0).color(1, 1, 1, 1).uv2(light).endVertex();
            vertex.vertex(stack.last().pose(), SIZE, 0, 0).color(1, 1, 1, 1).uv2(light).endVertex();
        } else {
            byte[] pixels = (byte[]) holder.getData();
            for (int x1 = 0; x1 < SIZE; x1++) {
                float x2 = x1 + 1;
                for (int y1 = 0; y1 < SIZE; y1++) {
                    float pixel = 0.0625f * (16 - pixels[x1 * SIZE + y1]);
                    float y2 = y1 + 1;
                    vertex.vertex(stack.last().pose(), x1, y1, 0).color(pixel, pixel, pixel, 1).uv2(light).endVertex();
                    vertex.vertex(stack.last().pose(), x1, y2, 0).color(pixel, pixel, pixel, 1).uv2(light).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y2, 0).color(pixel, pixel, pixel, 1).uv2(light).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y1, 0).color(pixel, pixel, pixel, 1).uv2(light).endVertex();
                }
            }
        }
    }

    @Override
    public void draw(PoseStack pPoseStack, MultiBufferSource pBuffer) {
        VertexConsumer vertex = pBuffer.getBuffer(COLOR_256);
        if (holder.isEmpty()) {
            vertex.vertex(pPoseStack.last().pose(), 0, 0, 0).color(1, 1, 1, 1).endVertex();
            vertex.vertex(pPoseStack.last().pose(), 0, SIZE, 0).color(1, 1, 1, 1).endVertex();
            vertex.vertex(pPoseStack.last().pose(), SIZE, SIZE, 0).color(1, 1, 1, 1).endVertex();
            vertex.vertex(pPoseStack.last().pose(), SIZE, 0, 0).color(1, 1, 1, 1).endVertex();
        } else {
            byte[] pixels = (byte[]) holder.getData();
            for (int x1 = 0; x1 < SIZE; x1++) {
                float x2 = x1 + 1;
                for (int y1 = 0; y1 < SIZE; y1++) {
                    float pixel = 0.0625f * (16 - pixels[x1 * SIZE + y1]);
                    float y2 = y1 + 1;
                    vertex.vertex(pPoseStack.last().pose(), x1, y1, 0).color(pixel, pixel, pixel, 1).endVertex();
                    vertex.vertex(pPoseStack.last().pose(), x1, y2, 0).color(pixel, pixel, pixel, 1).endVertex();
                    vertex.vertex(pPoseStack.last().pose(), x2, y2, 0).color(pixel, pixel, pixel, 1).endVertex();
                    vertex.vertex(pPoseStack.last().pose(), x2, y1, 0).color(pixel, pixel, pixel, 1).endVertex();
                }
            }
        }
    }
}
