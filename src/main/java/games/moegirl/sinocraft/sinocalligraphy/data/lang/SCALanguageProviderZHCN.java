package games.moegirl.sinocraft.sinocalligraphy.data.lang;

import games.moegirl.sinocraft.sinocalligraphy.item.RemovedItem;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocore.api.data.I18nProviderBase;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SCALanguageProviderZHCN extends I18nProviderBase {
    public SCALanguageProviderZHCN(DataGenerator gen, String modId, String mainModId, String locale) {
        super(gen, modId, mainModId, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(SCAItems.BRUSH, "毛笔");
        addItem(SCAItems.INK, "墨汁");
        addItem(SCAItems.XUAN_PAPER, "宣纸");

        add("itemGroup.sino_calligraphy", "华夏丹青");

        add(RemovedItem.HOVER_KEY, "已弃用，右键使用获取新物品");
        deprecated(SCAItems.XUAN_PAPER_FILLED, "宣纸");
        deprecated(SCAItems.EMPTY_XUAN_PAPER, "宣纸");
    }

    private void deprecated(Supplier<? extends Item> item, String name) {
        addItem(item, name + " (已弃用)");
    }
}
