package games.moegirl.sinocraft.sinocalligraphy.client;

import games.moegirl.sinocraft.sinocalligraphy.client.paper.FilledXuanPaperBlockRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SCAClient {

    public static BlockEntityWithoutLevelRenderer getXuanPaperRender() {
        return FilledXuanPaperBlockRenderer.getInstance();
    }
}
