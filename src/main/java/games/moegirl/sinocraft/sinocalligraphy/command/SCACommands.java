package games.moegirl.sinocraft.sinocalligraphy.command;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SinoCalligraphy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SCACommands {
    @SubscribeEvent
    public static void onServerStarting(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();

    }
}
