package games.moegirl.sinocraft.sinocalligraphy.network;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.DrawC2SPacket;
import net.minecraft.resources.ResourceLocation;
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

        INSTANCE.registerMessage(nextID(), DrawC2SPacket.class, DrawC2SPacket::serialize, DrawC2SPacket::new, DrawC2SPacket::handle);
    }
}
