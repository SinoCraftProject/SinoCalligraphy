package games.moegirl.sinocraft.sinocalligraphy.data.lang;

import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.item.FilledXuanPaper;
import games.moegirl.sinocraft.sinocore.api.data.I18nProviderBase;
import net.minecraft.data.DataGenerator;

public class SCALanguageProviderZHCN extends I18nProviderBase {
    public SCALanguageProviderZHCN(DataGenerator gen, String modId, String mainModId, String locale) {
        super(gen, modId, mainModId, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(SCAItems.BRUSH, "毛笔");
        addItem(SCAItems.INK, "墨汁");
        addItem(SCAItems.EMPTY_XUAN_PAPER, "宣纸");
        addItem(SCAItems.FILLED_XUAN_PAPER, "填充过的宣纸");

        add("itemGroup.sino_calligraphy", "华夏丹青");

        add(FilledXuanPaper.HOVER_AUTHOR_PREFIX, "作者：");
        add(FilledXuanPaper.HOVER_AUTHOR_EMPTY, "佚名");

        add(BrushGuiScreen.KEY_SAVE, "保存");
        add(BrushGuiScreen.KEY_SAVE_SUCCEED, "保存成功!");
        add(BrushGuiScreen.KEY_SAVE_ERR_INK, "墨汁不足");
        add(BrushGuiScreen.KEY_SAVE_ERR_PAPER, "宣纸不足");
    }
}
