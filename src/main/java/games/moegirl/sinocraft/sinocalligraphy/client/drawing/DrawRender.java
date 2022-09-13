package games.moegirl.sinocraft.sinocalligraphy.client.drawing;

import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import net.minecraft.client.renderer.MultiBufferSource;

/**
 * A renderer to render a draw
 */
public interface DrawRender {

    /**
     * Render the draw directly
     * qyl27: This was used for GUI.
     *
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
