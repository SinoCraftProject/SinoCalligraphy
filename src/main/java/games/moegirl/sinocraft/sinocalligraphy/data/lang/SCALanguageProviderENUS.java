package games.moegirl.sinocraft.sinocalligraphy.data.lang;

import games.moegirl.sinocraft.sinocalligraphy.item.RemovedItem;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocore.api.data.I18nProviderBase;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SCALanguageProviderENUS extends I18nProviderBase {
    public SCALanguageProviderENUS(DataGenerator gen, String modId, String mainModId, String locale) {
        super(gen, modId, mainModId, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(SCAItems.BRUSH, "Chinese Brush");
        addItem(SCAItems.INK, "Ink");
        addItem(SCAItems.XUAN_PAPER, "Xuan Paper");

        add("itemGroup.sino_calligraphy", "SinoCalligraphy");

        add(RemovedItem.HOVER_KEY, "Deprecated, please use it to get new item.");
        deprecated(SCAItems.XUAN_PAPER_FILLED, "Xuan Paper");
        deprecated(SCAItems.EMPTY_XUAN_PAPER, "Xuan Paper");
    }

    private void deprecated(Supplier<? extends Item> item, String name) {
        addItem(item, name + " (Deprecated Item)");
    }
}
