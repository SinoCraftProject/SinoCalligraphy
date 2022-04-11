package games.moegirl.sinocraft.sinocalligraphy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("sinocalligraphy")
public class SinoCalligraphy {
    public static final String MOD_ID = "sinocalligraphy";
    private static final Logger LOGGER = LogManager.getLogger();

    public SinoCalligraphy() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    }
}
