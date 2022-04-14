package games.moegirl.sinocraft.sinocalligraphy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public class SCAClient {
    private static BlockEntityWithoutLevelRenderer BEWLR;

    public static BlockEntityWithoutLevelRenderer getBEWLR() {
        if (BEWLR == null) {
            BEWLR = new SCABlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }
        return BEWLR;
    }

}
