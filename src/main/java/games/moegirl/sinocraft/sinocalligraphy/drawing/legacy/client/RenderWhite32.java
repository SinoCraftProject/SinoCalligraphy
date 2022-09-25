package games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.holder.HolderByte32;
import games.moegirl.sinocraft.sinocalligraphy.utility.DrawHelper;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import static games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.holder.HolderByte32.SIZE;
import static games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.version.VersionBrush3White.background;
import static games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.version.VersionBrush3White.foreground;

// todo todo todo todo
public class RenderWhite32 implements DrawRender {

    private final HolderByte32 holder;

    private final static RenderType RENDER = RenderType.create(
            "black_white_draw", DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS, 256, false, true,
            RenderType.CompositeState.builder().
                    setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader))
                    .createCompositeState(false));
    private final static RenderType RENDER_WITH_LIGHT = RenderType.create(
            "black_white_draw_with_light", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
            VertexFormat.Mode.QUADS, 256, false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorLightmapShader))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(true))
                    .createCompositeState(false));

    public RenderWhite32(HolderByte32 holder) {
        this.holder = holder;
    }

    @Override
    public void draw(PoseStack pPoseStack, int x, int y, int width, int height) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();

        GuiComponent.fill(pPoseStack, x, y, x + width, y + height, background);
        if (!holder.isEmpty()) {
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
                    var alpha = 16 * (16 - draw[index++]) - 1;
                    if (alpha != 255) {
                        var mixed = DrawHelper.mix(foreground, background, alpha);

                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        GuiComponent.fill(pPoseStack, x1, y1, x2, y2, mixed);
                    }
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
        RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
        VertexConsumer vertex = buffer.getBuffer(RENDER_WITH_LIGHT);

        if (holder.isEmpty()) {
            drawSquare(stack, vertex, background, 0, 0, SIZE, light);
        } else {
            byte[] pixels = (byte[]) holder.getData();
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    var alpha = 16 * (16 - pixels[x * SIZE + y]) - 1;

                    if (alpha == 255) {
                        drawSquare(stack, vertex, background, x, y, 1, light);
                    } else {
                        var mixed = DrawHelper.mix(foreground, background, alpha);
                        drawSquare(stack, vertex, mixed, x, y, 1, light);
                    }
                }
            }
        }
    }

    private void drawSquare(PoseStack stack, VertexConsumer vertex, int color, int x, int y, int size, int light) {
        vertex.vertex(stack.last().pose(), x, y, 0).color(color).uv2(light).endVertex();
        vertex.vertex(stack.last().pose(), x, y + size, 0).color(color).uv2(light).endVertex();
        vertex.vertex(stack.last().pose(), x + size, y + size, 0).color(color).uv2(light).endVertex();
        vertex.vertex(stack.last().pose(), x + size, y, 0).color(color).uv2(light).endVertex();
    }

    @Deprecated(since = "BrushV3")
    @Override
    public void draw(PoseStack pPoseStack, MultiBufferSource pBuffer) {
        VertexConsumer vertex = pBuffer.getBuffer(RENDER);
        if (holder.isEmpty()) {
            vertex.vertex(pPoseStack.last().pose(), 0, 0, 0).color(1f, 1f, 1f, 1f).endVertex();
            vertex.vertex(pPoseStack.last().pose(), 0, SIZE, 0).color(1f, 1f, 1f, 1f).endVertex();
            vertex.vertex(pPoseStack.last().pose(), SIZE, SIZE, 0).color(1f, 1f, 1f, 1f).endVertex();
            vertex.vertex(pPoseStack.last().pose(), SIZE, 0, 0).color(1f, 1f, 1f, 1f).endVertex();
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
