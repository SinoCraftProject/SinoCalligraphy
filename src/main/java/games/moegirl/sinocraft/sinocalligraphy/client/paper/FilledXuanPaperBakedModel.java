package games.moegirl.sinocraft.sinocalligraphy.client.paper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FilledXuanPaperBakedModel implements BakedModel {
    private final BakedModel existingModel;

    public FilledXuanPaperBakedModel(BakedModel existingModel) {
        this.existingModel = existingModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, Random pRand) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return existingModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return existingModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return existingModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return existingModel.getOverrides();
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack poseStack) {
        if (cameraTransformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                || cameraTransformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND
                || cameraTransformType == ItemTransforms.TransformType.FIXED) {
            return this;
        }

        return this.existingModel.handlePerspective(cameraTransformType, poseStack);
    }
}
