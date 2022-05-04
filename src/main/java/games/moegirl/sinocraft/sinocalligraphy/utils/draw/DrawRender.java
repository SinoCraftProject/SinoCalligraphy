package games.moegirl.sinocraft.sinocalligraphy.utils.draw;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface DrawRender {

    void draw(PoseStack pPoseStack, int x, int y, int width, int height);

    void draw(PoseStack stack, MultiBufferSource buffer, int packedLight);

    void draw(PoseStack stack, MultiBufferSource buffer);
}
