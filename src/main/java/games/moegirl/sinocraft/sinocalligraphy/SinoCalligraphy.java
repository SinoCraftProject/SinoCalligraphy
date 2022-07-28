package games.moegirl.sinocraft.sinocalligraphy;

import games.moegirl.sinocraft.sinocalligraphy.block.SCABlockItems;
import games.moegirl.sinocraft.sinocalligraphy.block.SCABlocks;
import games.moegirl.sinocraft.sinocalligraphy.fluid.SCAFluids;
import games.moegirl.sinocraft.sinocalligraphy.gui.SCAMenus;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.network.SCANetworks;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawVersions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod("sinocalligraphy")
public class SinoCalligraphy {
    public static final Map<String, Object> DEBUG = new HashMap<>();

    public static final String MODID = "sinocalligraphy";
    public static final String MC_VERSION = "1.18.2";
    public static final String MOD_VERSION = "1.0.3";
    public static final String VERSION = MC_VERSION + "-" + MOD_VERSION;
    private static final Logger LOGGER = LogManager.getLogger();

    public SinoCalligraphy() {
        LOGGER.info("Loading SinoCalligraphy. Ver: " + MOD_VERSION);

        LOGGER.info("The painting that used color to depict all walks of life, while retained black and white to render the beauty of ages.");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        SCAItems.register(bus);
        SCABlocks.register(bus);
        SCABlockItems.register(bus);
        SCAFluids.register(bus);
        SCAMenus.register(bus);
        SCANetworks.setup();

        DrawVersions.register();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
