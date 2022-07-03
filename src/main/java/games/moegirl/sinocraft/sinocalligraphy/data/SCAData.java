package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.block.SCABlockItems;
import games.moegirl.sinocraft.sinocalligraphy.block.SCABlocks;
import games.moegirl.sinocraft.sinocalligraphy.data.lang.SCALanguageProviderENUS;
import games.moegirl.sinocraft.sinocalligraphy.data.lang.SCALanguageProviderZHCN;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
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
            generator.addProvider(new SCABlockStateProvider(generator, SinoCalligraphy.MODID, exHelper, SCABlocks.BLOCKS));

            generator.addProvider(new SCAItemModelProvider(generator, SinoCalligraphy.MODID, exHelper, SCABlockItems.BLOCK_ITEMS));
            generator.addProvider(new SCAItemModelProvider(generator, SinoCalligraphy.MODID, exHelper, SCAItems.ITEMS));
        }

        if (event.includeServer()) {
            var blockTagsProvider = new SCABlockTagsProvider(generator, SinoCalligraphy.MODID, exHelper, SinoCalligraphy.MODID);

            generator.addProvider(blockTagsProvider);
            generator.addProvider(new SCAItemTagsProvider(generator, blockTagsProvider, SinoCalligraphy.MODID, exHelper));
            generator.addProvider(new SCARecipeProvider(generator));

            generator.addProvider(new SCALanguageProviderZHCN(generator, SinoCalligraphy.MODID, SinoCalligraphy.MODID, "zh_cn"));
            generator.addProvider(new SCALanguageProviderENUS(generator, SinoCalligraphy.MODID, SinoCalligraphy.MODID, "en_us"));
        }
    }
}
