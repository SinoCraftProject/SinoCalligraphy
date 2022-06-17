package games.moegirl.sinocraft.sinocalligraphy.data.lang;

import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
import games.moegirl.sinocraft.sinocalligraphy.item.FilledXuanPaper;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
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
        addItem(SCAItems.EMPTY_XUAN_PAPER, "Empty Xuan Paper");
        addItem(SCAItems.FILLED_XUAN_PAPER, "Filled Xuan Paper");

        add("itemGroup.sino_calligraphy", "SinoCalligraphy");

        add(FilledXuanPaper.HOVER_AUTHOR_PREFIX, "Author: ");

        add(DrawHolder.KEY_NO_AUTHOR, "Unknown");

        add(BrushGuiScreen.KEY_SAVE, "Save");
        add(BrushGuiScreen.KEY_SAVE_SUCCEED, "Save Succeed!");
        add(BrushGuiScreen.KEY_SAVE_ERR_INK, "No enough ink");
        add(BrushGuiScreen.KEY_SAVE_ERR_PAPER, "No enough paper");
        add(BrushGuiScreen.KEY_COPY, "Left click to copy, right click to paste");
        add(BrushGuiScreen.KEY_COPIED, "Copied");
        add(BrushGuiScreen.KEY_PASTE_FAILED, "Failed to paste draw %s");
        add(BrushGuiScreen.KEY_OUTPUT, "Output to file");
        add(BrushGuiScreen.KEY_OUTPUT_SUCCEED, "Output succeed");
        add(BrushGuiScreen.KEY_OUTPUT_FAILED, "Output failed: %s");
    }
}
