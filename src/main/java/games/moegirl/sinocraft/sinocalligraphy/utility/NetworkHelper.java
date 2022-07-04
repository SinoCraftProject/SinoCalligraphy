package games.moegirl.sinocraft.sinocalligraphy.utility;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.SaveFailedS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class NetworkHelper {
    public static void onClientHandleDraw(SaveFailedS2CPacket.Reason reason, int button) {
        Player player = Minecraft.getInstance().player;
        if (player.containerMenu instanceof BrushMenu container) {
            container.gui.handleSaveResult(reason, button);
        }
    }
}
