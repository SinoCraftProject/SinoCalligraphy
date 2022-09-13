package games.moegirl.sinocraft.sinocalligraphy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import games.moegirl.sinocraft.sinocalligraphy.drawing.client.DrawRenders;
import games.moegirl.sinocraft.sinocalligraphy.drawing.Constants;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.holder.HolderByte32;
import games.moegirl.sinocraft.sinocalligraphy.drawing.version.DrawVersions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * BlockEntity Without Level Renderer is the new ISTER.
 * A mod should only one BEWLR.
 * @author qyl27
 */
public class XuanPaperRenderer extends BlockEntityWithoutLevelRenderer {

    @Nullable
    private static XuanPaperRenderer INSTANCE;

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
        var tag = stack.getOrCreateTag();
        CompoundTag nbt = null;
        if (tag.contains(Constants.TAG_HOLDER)) {
            nbt = tag.getCompound(Constants.TAG_HOLDER);
        } else {
            nbt = new CompoundTag();
        }

        DrawHolder holder = DrawHolder.parse(nbt, DrawVersions.LATEST_BRUSH_VERSION).orElseGet(DrawVersions.LATEST_BRUSH_VERSION::newDraw);
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
            poseStack.scale(HolderByte32.SIZE, HolderByte32.SIZE, HolderByte32.SIZE);
        }
        DrawRenders.of(holder).draw(poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
