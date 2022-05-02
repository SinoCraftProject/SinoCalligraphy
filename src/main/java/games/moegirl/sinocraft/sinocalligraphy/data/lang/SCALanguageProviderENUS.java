package games.moegirl.sinocraft.sinocalligraphy.data.lang;

import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem;
import games.moegirl.sinocraft.sinocore.api.data.I18nProviderBase;
import net.minecraft.data.DataGenerator;

public class SCALanguageProviderENUS extends I18nProviderBase {
    public SCALanguageProviderENUS(DataGenerator gen, String modId, String mainModId, String locale) {
        super(gen, modId, mainModId, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(SCAItems.BRUSH, "Chinese Brush");
        addItem(SCAItems.INK, "Ink");
        addItem(SCAItems.EMPTY_XUAN_PAPER, "Xuan Paper");
        addItem(SCAItems.XUAN_PAPER_FILLED, "Xuan Paper (Filled)");

        add("itemGroup.sino_calligraphy", "SinoCalligraphy");

        add(XuanPaperItem.HOVER_AUTHOR, "Author: ");
        add(XuanPaperItem.HOVER_AUTHOR_ERROR, "%s");
        add(XuanPaperItem.HOVER_AUTHOR_EMPTY, "Unknown");

        add(BrushGuiScreen.KEY_SAVE, "Save");
        add(BrushGuiScreen.KEY_SAVE_SUCCEED, "Save Succeed!");
        add(BrushGuiScreen.KEY_SAVE_ERR_INK, "No enough ink");
        add(BrushGuiScreen.KEY_SAVE_ERR_PAPER, "No enough paper");
    }
}
