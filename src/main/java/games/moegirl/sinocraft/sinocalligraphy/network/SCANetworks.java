package games.moegirl.sinocraft.sinocalligraphy.network;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.DrawSaveC2SPacket;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.SaveFailedS2CPacket;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.SaveSuccessS2CClient;
import games.moegirl.sinocraft.sinocore.api.utility.Id;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class SCANetworks {
    public static SimpleChannel INSTANCE;
    private static final Id ID = new Id();

    public static void setup() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(SinoCalligraphy.MODID, "network"),
                () -> SinoCalligraphy.VERSION,
                SinoCalligraphy.VERSION::equals,
                SinoCalligraphy.VERSION::equals
        );

        INSTANCE.registerMessage(ID.nextId(), DrawSaveC2SPacket.class, DrawSaveC2SPacket::serialize, DrawSaveC2SPacket::new, DrawSaveC2SPacket::handle);
        INSTANCE.registerMessage(ID.nextId(), SaveFailedS2CPacket.class, SaveFailedS2CPacket::serialize, SaveFailedS2CPacket::new, SaveFailedS2CPacket::handle);
        INSTANCE.registerMessage(ID.nextId(), SaveSuccessS2CClient.class, SaveSuccessS2CClient::serialize, SaveSuccessS2CClient::new, SaveSuccessS2CClient::handle);
    }

    public static void send(Object message) {
        INSTANCE.sendToServer(message);
    }

    public static void send(Object message, Player player) {
        INSTANCE.sendTo(message, ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
