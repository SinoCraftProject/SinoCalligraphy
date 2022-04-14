package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.data.lang.SCALanguageProviderENUS;
import games.moegirl.sinocraft.sinocalligraphy.data.lang.SCALanguageProviderZHCN;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.data.ItemModelProviderBase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = SinoCalligraphy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCAData {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var exHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            generator.addProvider(new ItemModelProviderBase(generator, SinoCalligraphy.MODID, exHelper, SCAItems.ITEMS));
        }

        if (event.includeServer()) {
            var blockTagsProvider = new SCABlockTagsProvider(generator, SinoCalligraphy.MODID, exHelper, SinoCore.MODID);

            generator.addProvider(blockTagsProvider);
            generator.addProvider(new SCAItemTagsProvider(generator, blockTagsProvider, SinoCalligraphy.MODID, exHelper));

            generator.addProvider(new SCALanguageProviderZHCN(generator, SinoCalligraphy.MODID, SinoCore.MODID, "zh_cn"));
            generator.addProvider(new SCALanguageProviderENUS(generator, SinoCalligraphy.MODID, SinoCore.MODID, "en_us"));
        }
    }
}
