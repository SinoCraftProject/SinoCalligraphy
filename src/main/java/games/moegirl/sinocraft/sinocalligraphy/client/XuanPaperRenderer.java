package games.moegirl.sinocraft.sinocalligraphy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * BlockEntity Without Level Renderer is the new ISTER.
 * A mod should only one BEWLR.
 * @author qyl27
 */
@OnlyIn(Dist.CLIENT)
public class XuanPaperRenderer extends BlockEntityWithoutLevelRenderer {

    @Nullable
    private static XuanPaperRenderer INSTANCE;
    private final static RenderType XUAN_RENDER = RenderType.create(
            XuanPaperItem.TAG_PIXELS, DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS, 256, false, true,
            CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader)).createCompositeState(false));
    private final static RenderType XUAN_RENDER_WITH_LIGHT = RenderType.create(
            XuanPaperItem.TAG_PIXELS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
            VertexFormat.Mode.QUADS, 256, false, true,
            CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorLightmapShader))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(true))
                    .createCompositeState(false));

    public static XuanPaperRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XuanPaperRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }
        return INSTANCE;
    }

    XuanPaperRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        poseStack.pushPose();
        if (transformType == ItemTransforms.TransformType.FIXED) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            poseStack.scale(1, -1, 1);
            poseStack.translate(-1.5, -1.5, -0.5);
            poseStack.scale(0.0625f, 0.0625f, 0.0625f);
            poseStack.translate(0.0D, 0.0D, 0.01D);
        } else {
            poseStack.scale(0.03125f, 0.03125f, 1.0f);
            poseStack.scale(XuanPaperItem.SIZE, XuanPaperItem.SIZE, XuanPaperItem.SIZE);
        }
        renderXuanPaper(poseStack, buffer, packedLight, stack);
        poseStack.popPose();
    }

    public static void renderXuanPaper(PoseStack stack, MultiBufferSource buffer, int packedLight, ItemStack item) {
        VertexConsumer vertex = buffer.getBuffer(XUAN_RENDER_WITH_LIGHT);
        if (XuanPaperItem.hasDraw(item)) {
            byte[] pixels = XuanPaperItem.getDraw(item);
            for (int x1 = 0; x1 < XuanPaperItem.SIZE; x1++) {
                float x2 = x1 + 1;
                for (int y1 = 0; y1 < XuanPaperItem.SIZE; y1++) {
                    float pixel = 0.0625f * (16 - pixels[x1 * XuanPaperItem.SIZE + y1]);
                    float y2 = y1 + 1;
                    vertex.vertex(stack.last().pose(), x1, y1, 0).color(pixel, pixel, pixel, 1).uv2(packedLight).endVertex();
                    vertex.vertex(stack.last().pose(), x1, y2, 0).color(pixel, pixel, pixel, 1).uv2(packedLight).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y2, 0).color(pixel, pixel, pixel, 1).uv2(packedLight).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y1, 0).color(pixel, pixel, pixel, 1).uv2(packedLight).endVertex();
                }
            }
        } else {
            vertex.vertex(stack.last().pose(), 0, 0, 0).color(0, 0, 0, 1).uv2(packedLight).endVertex();
            vertex.vertex(stack.last().pose(), 0, XuanPaperItem.SIZE, 0).color(0, 0, 0, 1).uv2(packedLight).endVertex();
            vertex.vertex(stack.last().pose(), XuanPaperItem.SIZE, XuanPaperItem.SIZE, 0).color(0, 0, 0, 1).uv2(packedLight).endVertex();
            vertex.vertex(stack.last().pose(), XuanPaperItem.SIZE, 0, 0).color(0, 0, 0, 1).uv2(packedLight).endVertex();
        }
    }

    public static void renderXuanPaper(PoseStack stack, MultiBufferSource buffer, ItemStack item) {
        VertexConsumer vertex = buffer.getBuffer(XUAN_RENDER);
        if (XuanPaperItem.hasDraw(item)) {
            byte[] pixels = XuanPaperItem.getDraw(item);
            for (int x1 = 0; x1 < XuanPaperItem.SIZE; x1++) {
                float x2 = x1 + 1;
                for (int y1 = 0; y1 < XuanPaperItem.SIZE; y1++) {
                    float pixel = 0.0625f * (16 - pixels[x1 * XuanPaperItem.SIZE + y1]);
                    float y2 = y1 + 1;
                    vertex.vertex(stack.last().pose(), x1, y1, 0).color(pixel, pixel, pixel, 1).endVertex();
                    vertex.vertex(stack.last().pose(), x1, y2, 0).color(pixel, pixel, pixel, 1).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y2, 0).color(pixel, pixel, pixel, 1).endVertex();
                    vertex.vertex(stack.last().pose(), x2, y1, 0).color(pixel, pixel, pixel, 1).endVertex();
                }
            }
        } else {
            vertex.vertex(stack.last().pose(), 0, 0, 0).color(0, 0, 0, 1).endVertex();
            vertex.vertex(stack.last().pose(), 0, XuanPaperItem.SIZE, 0).color(0, 0, 0, 1).endVertex();
            vertex.vertex(stack.last().pose(), XuanPaperItem.SIZE, XuanPaperItem.SIZE, 0).color(0, 0, 0, 1).endVertex();
            vertex.vertex(stack.last().pose(), XuanPaperItem.SIZE, 0, 0).color(0, 0, 0, 1).endVertex();
        }
    }
}
