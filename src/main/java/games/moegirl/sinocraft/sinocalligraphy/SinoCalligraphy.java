package games.moegirl.sinocraft.sinocalligraphy;

import games.moegirl.sinocraft.sinocalligraphy.gui.SCAMenus;
import games.moegirl.sinocraft.sinocalligraphy.gui.texture.DebugCommand;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.network.SCANetworks;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawVersions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("sinocalligraphy")
public class SinoCalligraphy {
    public static final String MODID = "sinocalligraphy";
    public static final String VERSION = "1.18.2-1.0.2";
    private static final Logger LOGGER = LogManager.getLogger();

    public SinoCalligraphy() {
        LOGGER.info("Loading SinoCalligraphy.");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        SCAItems.register(bus);
        SCAMenus.register(bus);
        SCANetworks.setup();

        DrawVersions.register();

        DebugCommand.REGISTRY.register();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
