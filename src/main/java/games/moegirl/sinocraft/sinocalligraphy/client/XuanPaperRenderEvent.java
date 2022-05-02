package games.moegirl.sinocraft.sinocalligraphy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client render event subscriber.
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class XuanPaperRenderEvent {

    public static final ModelResourceLocation MAP_MODEL_LOCATION = new ModelResourceLocation(new ResourceLocation("minecraft", "item_frame"), "map=true");

    @SubscribeEvent
    public static void onRenderInFrame(RenderItemInFrameEvent event) {
        var item = event.getItemStack();
        var frame = event.getItemFrameEntity();
        if (item.is(SCAItems.XUAN_PAPER_FILLED.get())) {
            // Re-render the frame, in order to prevent asm to net.minecraft.client.renderer.entity.ItemFrameRenderer.render.
            var isInvisible = frame.isInvisible();
            var mc = Minecraft.getInstance();
            var stack = event.getPoseStack();
            var buffers = event.getMultiBufferSource();
            var light = event.getPackedLight();

            if (!isInvisible) {
                var vec = Vec3.ZERO.scale(-45 * frame.getRotation());
                stack.scale((float) vec.x, (float) vec.y, (float) vec.z);
                stack.translate(0, 0, -0.4375);
                BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
                BakedModel model = dispatcher.getBlockModelShaper().getModelManager().getModel(MAP_MODEL_LOCATION);
                VertexConsumer consumer = buffers.getBuffer(Sheets.solidBlockSheet());
                stack.pushPose();
                stack.translate(-0.5D, -0.5D, -0.5D);
                dispatcher.getModelRenderer().renderModel(stack.last(), consumer, null, model,
                        1.0F, 1.0F, 1.0F, light, OverlayTexture.NO_OVERLAY);
                stack.popPose();
                stack.translate(0.0D, 0.0D, 0.4375D);
            }

            var vec2 = Vec3.ZERO.scale(frame.getRotation() % 4 * 90);
            stack.scale((float) vec2.x, (float) vec2.y, (float) vec2.z);
        }
    }

    /**
     * @see net.minecraft.client.renderer.ItemInHandRenderer#renderHandsWithItems(float, PoseStack, MultiBufferSource.BufferSource, LocalPlayer, int)
     */
    @SubscribeEvent
    public static void onRenderInHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ItemStack stack = event.getItemStack();
        if (stack.is(SCAItems.XUAN_PAPER_FILLED.get()) && player != null && !player.isScoping()) {
            InteractionHand hand = event.getHand();
            switch (hand) {
                case MAIN_HAND -> {
                    if (player.getOffhandItem().isEmpty()) renderTwoHandedPaper(player, event);
                    else renderOneHandedPaper(player, event, player.getMainArm());
                }
                case OFF_HAND -> renderOneHandedPaper(player, event, player.getMainArm().getOpposite());
            }
            event.setCanceled(true);
        }
    }

    private static void renderXuanPaperInHand(PoseStack pMatrixStack, MultiBufferSource pBuffer, ItemStack pStack) {
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        pMatrixStack.scale(0.38F, 0.38F, 0.38F);
        pMatrixStack.translate(-0.5D, -0.5D, 0.0D);
        pMatrixStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
        float step = 128f / XuanPaperItem.SIZE;
        pMatrixStack.scale(step, step, step);
        XuanPaperRenderer.renderXuanPaper(pMatrixStack, pBuffer, pStack);
    }

    private static void renderOneHandedPaper(LocalPlayer player, RenderHandEvent event, HumanoidArm arm) {
        PoseStack stack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();
        int light = event.getPackedLight();
        float equippedProgress = event.getEquipProgress();
        float swingProgress = event.getSwingProgress();

        float f = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        stack.translate(f * 0.125F, -0.125D, 0.0D);
        if (!player.isInvisible()) {
            stack.pushPose();
            stack.mulPose(Vector3f.ZP.rotationDegrees(f * 10.0F));
            renderPlayerArm(player, stack, buffer, light, equippedProgress, swingProgress, arm);
            stack.popPose();
        }

        stack.pushPose();
        stack.translate(f * 0.51F, -0.08F + equippedProgress * -1.2F, -0.75D);
        float f1 = Mth.sqrt(swingProgress);
        float f2 = Mth.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * Mth.sin(swingProgress * (float) Math.PI);
        stack.translate(f * f3, f4 - 0.3F * f2, f5);
        stack.mulPose(Vector3f.XP.rotationDegrees(f2 * -45.0F));
        stack.mulPose(Vector3f.YP.rotationDegrees(f * f2 * -30.0F));
        renderXuanPaperInHand(stack, buffer, event.getItemStack());
        stack.popPose();
    }

    private static void renderTwoHandedPaper(LocalPlayer player, RenderHandEvent event) {
        PoseStack stack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();
        int light = event.getPackedLight();
        float equippedProgress = event.getEquipProgress();
        float swingProgress = event.getSwingProgress();

        stack.pushPose();
        float f = Mth.sqrt(swingProgress);
        float f1 = -0.2F * Mth.sin(swingProgress * (float) Math.PI);
        float f2 = -0.4F * Mth.sin(f * (float) Math.PI);
        stack.translate(0.0D, -f1 / 2.0F, f2);
        float f3 = calculateMapTilt(event.getInterpolatedPitch());
        stack.translate(0.0D, 0.04F + equippedProgress * -1.2F + f3 * -0.5F, -0.72F);
        stack.mulPose(Vector3f.XP.rotationDegrees(f3 * -85.0F));
        if (!player.isInvisible()) {
            stack.pushPose();
            stack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            renderMapHand(player, stack, buffer, light, HumanoidArm.RIGHT);
            renderMapHand(player, stack, buffer, light, HumanoidArm.LEFT);
            stack.popPose();
        }

        float f4 = Mth.sin(f * (float) Math.PI);
        stack.mulPose(Vector3f.XP.rotationDegrees(f4 * 20.0F));
        stack.scale(2.0F, 2.0F, 2.0F);
        renderXuanPaperInHand(stack, buffer, event.getItemStack());
        stack.popPose();
    }

    private static void renderPlayerArm(LocalPlayer player, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, float pEquippedProgress, float pSwingProgress, HumanoidArm pSide) {
        boolean flag = pSide != HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(pSwingProgress);
        float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * Mth.sin(pSwingProgress * (float) Math.PI);
        pMatrixStack.translate(f * (f2 + 0.64), f3 - 0.6 + pEquippedProgress * -0.6F, f4 - 0.72);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(pSwingProgress * pSwingProgress * (float) Math.PI);
        float f6 = Mth.sin(f1 * (float) Math.PI);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
        RenderSystem.setShaderTexture(0, player.getSkinTextureLocation());
        pMatrixStack.translate(f * -1.0F, 3.6F, 3.5D);
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(f * 120.0F));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(200.0F));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f * -135.0F));
        pMatrixStack.translate(f * 5.6F, 0.0D, 0.0D);
        PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        if (flag) {
            renderer.renderRightHand(pMatrixStack, pBuffer, pCombinedLight, player);
        } else {
            renderer.renderLeftHand(pMatrixStack, pBuffer, pCombinedLight, player);
        }
    }

    private static float calculateMapTilt(float pitch) {
        float f = 1.0F - pitch / 45.0F + 0.1F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        return -Mth.cos(f * (float) Math.PI) * 0.5F + 0.5F;
    }

    private static void renderMapHand(LocalPlayer player, PoseStack stack, MultiBufferSource buffer, int light, HumanoidArm arm) {
        RenderSystem.setShaderTexture(0, player.getSkinTextureLocation());
        PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        stack.pushPose();
        float f = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        stack.mulPose(Vector3f.YP.rotationDegrees(92.0F));
        stack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(f * -41.0F));
        stack.translate(f * 0.3, -1.1, 0.45);
        if (arm == HumanoidArm.RIGHT) {
            renderer.renderRightHand(stack, buffer, light, player);
        } else {
            renderer.renderLeftHand(stack, buffer, light, player);
        }

        stack.popPose();
    }

}