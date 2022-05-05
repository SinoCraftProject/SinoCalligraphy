package games.moegirl.sinocraft.sinocalligraphy.utils.draw;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A renderer to render a draw
 */
@OnlyIn(Dist.CLIENT)
public interface DrawRender {

    /**
     * Render the draw directly
     * @param pPoseStack stack
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     */
    void draw(PoseStack pPoseStack, int x, int y, int width, int height);

    /**
     * Render the draw to a buffer with light
     * @param pPoseStack stack
     * @param pBuffer buffer
     * @param pPackedLight light data
     */
    void draw(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight);

    /**
     * Render the draw to a buffer
     * @param pPoseStack stack
     * @param pBuffer buffer
     */
    void draw(PoseStack pPoseStack, MultiBufferSource pBuffer);
}
