package games.moegirl.sinocraft.sinocalligraphy.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client render event subscriber.
 */
@Mod.EventBusSubscriber(modid = SinoCalligraphy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class SCAClient {
    private static BlockEntityWithoutLevelRenderer BEWLR;

    public static final ModelResourceLocation MAP_MODEL_LOCATION = new ModelResourceLocation(new ResourceLocation("minecraft", "item_frame"), "map=true");

    public static BlockEntityWithoutLevelRenderer getBEWLR() {
        if (BEWLR == null) {
            BEWLR = new SCABlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }
        return BEWLR;
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void onRenderInFrame(RenderItemInFrameEvent event) {
        var item = event.getItemStack();
        var frame = event.getItemFrameEntity();
        if (!item.isEmpty() && item.is(SCAItems.XUAN_PAPER.get())) {
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
}
