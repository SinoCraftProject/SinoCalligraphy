package games.moegirl.sinocraft.sinocalligraphy.client.paper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import games.moegirl.sinocraft.sinocalligraphy.item.FilledXuanPaper;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

/**
 * BlockEntity Without Level Renderer is the new ISTER.
 * A mod should only one BEWLR.
 * @author qyl27
 */
@OnlyIn(Dist.CLIENT)
public class FilledXuanPaperBlockRenderer extends BlockEntityWithoutLevelRenderer {

    private static FilledXuanPaperBlockRenderer INSTANCE = null;

    public synchronized static FilledXuanPaperBlockRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FilledXuanPaperBlockRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }
        return INSTANCE;
    }

    FilledXuanPaperBlockRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        renderXuanPaperAsItem(stack, transformType, poseStack, buffer, packedLight);
    }

    private void renderXuanPaperAsItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int light) {
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
            poseStack.scale(FilledXuanPaper.SIZE, FilledXuanPaper.SIZE, FilledXuanPaper.SIZE);
        }
        renderXuanPaper(poseStack, buffer, light, stack);
        poseStack.popPose();
    }

    public static void renderXuanPaper(PoseStack stack, MultiBufferSource buffer, int combinedLight, ItemStack item) {
        if (!item.is(SCAItems.FILLED_XUAN_PAPER.get())) {
            return;
        }
        
        byte[] pixels;
        if (item.hasTag() && item.getOrCreateTag().contains(FilledXuanPaper.TAG_PIXELS, Tag.TAG_BYTE_ARRAY)) {
            pixels = item.getOrCreateTag().getByteArray(FilledXuanPaper.TAG_PIXELS);
        } else {
            pixels = new byte[FilledXuanPaper.SIZE * FilledXuanPaper.SIZE];
            Arrays.fill(pixels, (byte) 0);
        }

        VertexConsumer vertex = buffer.getBuffer(RenderType.create(
                FilledXuanPaper.TAG_PIXELS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
                VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorLightmapShader))
                        .setLightmapState(new RenderStateShard.LightmapStateShard(true))
                        .createCompositeState(false)));

        for (int x1 = 0; x1 < FilledXuanPaper.SIZE; x1++) {
            float x2 = x1 + 1;
            for (int y1 = 0; y1 < FilledXuanPaper.SIZE; y1++) {
                float pixel = 0.0625f * (16 - pixels[x1 * FilledXuanPaper.SIZE + y1]);
                float y2 = y1 + 1;
                vertex.vertex(stack.last().pose(), x1, y1, 0).color(pixel, pixel, pixel, 1).uv2(combinedLight).endVertex();
                vertex.vertex(stack.last().pose(), x1, y2, 0).color(pixel, pixel, pixel, 1).uv2(combinedLight).endVertex();
                vertex.vertex(stack.last().pose(), x2, y2, 0).color(pixel, pixel, pixel, 1).uv2(combinedLight).endVertex();
                vertex.vertex(stack.last().pose(), x2, y1, 0).color(pixel, pixel, pixel, 1).uv2(combinedLight).endVertex();
            }
        }
    }
}
