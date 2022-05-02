package games.moegirl.sinocraft.sinocalligraphy.network;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.C2SSaveDraw;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.S2CSaveFailed;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.S2CSaveSucceed;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class SCANetworks {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void setup() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(SinoCalligraphy.MODID, "network"),
                () -> SinoCalligraphy.VERSION,
                SinoCalligraphy.VERSION::equals,
                SinoCalligraphy.VERSION::equals
        );

        INSTANCE.registerMessage(nextID(), C2SSaveDraw.class, C2SSaveDraw::serialize, C2SSaveDraw::new, C2SSaveDraw::handle);
        INSTANCE.registerMessage(nextID(), S2CSaveFailed.class, S2CSaveFailed::serialize, S2CSaveFailed::new, S2CSaveFailed::handle);
        INSTANCE.registerMessage(nextID(), S2CSaveSucceed.class, S2CSaveSucceed::serialize, S2CSaveSucceed::new, S2CSaveSucceed::handle);
    }

    public static void send(Object message) {
        INSTANCE.sendToServer(message);
    }

    public static void send(Object message, Player player) {
        INSTANCE.sendTo(message, ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
