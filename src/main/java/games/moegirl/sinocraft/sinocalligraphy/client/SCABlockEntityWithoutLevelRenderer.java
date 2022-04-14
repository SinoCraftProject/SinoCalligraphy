package games.moegirl.sinocraft.sinocalligraphy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;

/**
 * BlockEntity Without Level Renderer is the new ISTER.
 * A mod should only one BEWLR.
 * @author qyl27
 */
public class SCABlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    public SCABlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (stack.is(SCAItems.XUAN_PAPER.get())) {
            renderXuanPaper(stack, transformType, poseStack, buffer);
        }

        // qyl27: Add other renderer to here.
    }

    private void renderXuanPaper(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer) {
        if (stack.getTag() == null) {
            return;
        }

        var renderType = RenderType.create(
                BrushGuiScreen.PIXELS_TAG_NAME, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
                VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState
                        .builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeTextShader))
                        .createCompositeState(false));

        VertexConsumer builder = buffer.getBuffer(renderType);
        byte[] pixels = stack.getTag().getByteArray(BrushGuiScreen.PIXELS_TAG_NAME);

        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();

        poseStack.pushPose();

        if (transformType == ItemTransforms.TransformType.FIXED) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            poseStack.translate(-1.5, -0.5, -0.5);
            poseStack.scale(0.0625f, 0.0625f, 0.0625f);
            poseStack.translate(0.0D, 0.0D, 0.01D);
        } else {
            poseStack.scale(0.03125f, 0.03125f, 1.0f);
        }

        for (int i = 0; i < BrushGuiScreen.CANVAS_SIZE; i++) {
            for (int j = 0; j < BrushGuiScreen.CANVAS_SIZE; j++) {
                float color = 0.0625f * (16 - pixels[i * BrushGuiScreen.CANVAS_SIZE + j]);
                poseStack.pushPose();
                poseStack.translate(i, BrushGuiScreen.CANVAS_SIZE - 1 - j, 0);
                RenderHelper.addSquare(builder, poseStack,
                        new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector4f(color, color, color, 1.0f), null);
                poseStack.popPose();
            }
        }
        poseStack.popPose();
    }
}
