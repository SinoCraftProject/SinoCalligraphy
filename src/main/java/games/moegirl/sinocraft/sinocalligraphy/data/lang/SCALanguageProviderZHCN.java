package games.moegirl.sinocraft.sinocalligraphy.data.lang;

import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem;
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
        addItem(SCAItems.XUAN_PAPER_FILLED, "宣纸 (已绘画)");

        add("itemGroup.sino_calligraphy", "华夏丹青");

        add(XuanPaperItem.HOVER_AUTHOR, "作者: ");
        add(XuanPaperItem.HOVER_AUTHOR_ERROR, "%s");
        add(XuanPaperItem.HOVER_AUTHOR_EMPTY, "未知");

        add(BrushGuiScreen.KEY_SAVE, "保存");
        add(BrushGuiScreen.KEY_SAVE_SUCCEED, "保存成功!");
        add(BrushGuiScreen.KEY_SAVE_ERR_INK, "墨汁不足");
        add(BrushGuiScreen.KEY_SAVE_ERR_PAPER, "宣纸不足");
    }
}
