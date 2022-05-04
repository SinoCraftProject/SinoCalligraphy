package games.moegirl.sinocraft.sinocalligraphy.utils.draw;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

import static games.moegirl.sinocraft.sinocalligraphy.utils.draw.SmallBlackWhiteBrushHolder.SIZE;

@OnlyIn(Dist.CLIENT)
public class SmallBlockWhiteBrushRender implements DrawRender {

    private final SmallBlackWhiteBrushHolder holder;
    private final static RenderType RENDER = RenderType.create(
            BrushV1Version.TAG_PIXELS, DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS, 256, false, true,
            RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader)).createCompositeState(false));
    private final static RenderType RENDER_WITH_LIGHT = RenderType.create(
            BrushV1Version.TAG_PIXELS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
            VertexFormat.Mode.QUADS, 256, false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorLightmapShader))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(true))
                    .createCompositeState(false));

    public SmallBlockWhiteBrushRender(SmallBlackWhiteBrushHolder holder) {
        this.holder = holder;
    }

    @Override
    public void draw(PoseStack pPoseStack, int x, int y, int width, int height) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        if (holder.isEmpty()) {
            GuiComponent.fill(pPoseStack, x, y, x + width, y + height, Color.WHITE.getRGB());
        } else {
            int unitX = width / SIZE;
            int unitY = height / SIZE;
            int x1 = x;
            int x2 = x1 + unitX;
            byte[] draw = holder.getDraw();
            int index = 0;
            for (int i = 0; i < SIZE; i++) {
                int y1 = y;
                int y2 = y1 + unitY;
                for (int j = 0; j < SIZE; j++) {
                    float color = 0.0625f * (16 - draw[index++]); // qyl27: For calculating grayscale.
                    RenderSystem.setShaderColor(color, color, color, 1.0f);
                    GuiComponent.fill(pPoseStack, x1, y1, x2, y2, new Color(color, color, color, 1).getRGB());
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
    public void draw(PoseStack stack, MultiBufferSource buffer, int packedLight) {
        VertexConsumer vertex = buffer.getBuffer(RENDER_WITH_LIGHT);
        if (holder.isEmpty()) {
            vertex.vertex(stack.last().pose(), 0, 0, 0).color(0, 0, 0, 1).uv2(packedLight).endVertex();
            vertex.vertex(stack.last().pose(), 0, SIZE, 0).color(0, 0, 0, 1).uv2(packedLight).endVertex();
            vertex.vertex(stack.last().pose(), SIZE, SIZE, 0).color(0, 0, 0, 1).uv2(packedLight).endVertex();
            vertex.vertex(stack.last().pose(), SIZE, 0, 0).color(0, 0, 0, 1).uv2(packedLight).endVertex();
        } else {
            byte[] pixels = holder.getDraw();
            for (int x1 = 0; x1 < SIZE; x1++) {
                float x2 = x1 + 1;
                for (int y1 = 0; y1 < SIZE; y1++) {
                    float pixel = 0.0625f * (16 - pixels[x1 * SIZE + y1]);
                    float y2 = y1 + 1;
                    vertex.vertex(stack.last().pose(), x1, y1, 0).color(pixel, pixel, pixel, 1).uv2(packedLight).endVertex();
                    vertex.vertex(stack.last().pose(), x1, y2, 0).color(pixel, pixel, pixel, 1).uv2(packedLight).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y2, 0).color(pixel, pixel, pixel, 1).uv2(packedLight).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y1, 0).color(pixel, pixel, pixel, 1).uv2(packedLight).endVertex();
                }
            }
        }
    }

    @Override
    public void draw(PoseStack stack, MultiBufferSource buffer) {
        VertexConsumer vertex = buffer.getBuffer(RENDER);
        if (holder.isEmpty()) {
            vertex.vertex(stack.last().pose(), 0, 0, 0).color(0, 0, 0, 1).endVertex();
            vertex.vertex(stack.last().pose(), 0, SIZE, 0).color(0, 0, 0, 1).endVertex();
            vertex.vertex(stack.last().pose(), SIZE, SIZE, 0).color(0, 0, 0, 1).endVertex();
            vertex.vertex(stack.last().pose(), SIZE, 0, 0).color(0, 0, 0, 1).endVertex();
        } else {
            byte[] pixels = holder.getDraw();
            for (int x1 = 0; x1 < SIZE; x1++) {
                float x2 = x1 + 1;
                for (int y1 = 0; y1 < SIZE; y1++) {
                    float pixel = 0.0625f * (16 - pixels[x1 * SIZE + y1]);
                    float y2 = y1 + 1;
                    vertex.vertex(stack.last().pose(), x1, y1, 0).color(pixel, pixel, pixel, 1).endVertex();
                    vertex.vertex(stack.last().pose(), x1, y2, 0).color(pixel, pixel, pixel, 1).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y2, 0).color(pixel, pixel, pixel, 1).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y1, 0).color(pixel, pixel, pixel, 1).endVertex();
                }
            }
        }
    }
}
