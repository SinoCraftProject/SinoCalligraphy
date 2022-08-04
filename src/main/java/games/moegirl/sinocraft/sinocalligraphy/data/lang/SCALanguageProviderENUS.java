package games.moegirl.sinocraft.sinocalligraphy.data.lang;

import games.moegirl.sinocraft.sinocalligraphy.block.SCABlocks;
import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
import games.moegirl.sinocraft.sinocalligraphy.item.*;
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
        addItem(SCAItems.EMPTY_XUAN_PAPER_RED, "Red Xuan Paper");
        addItem(SCAItems.EMPTY_XUAN_PAPER_BLACK, "Black Xuan Paper");
        addItem(SCAItems.FILLED_XUAN_PAPER, "Filled Xuan Paper");
        addItem(SCAItems.WOOD_PULP_BUCKET, "Bucket of wood pulp");
        addItem(SCAItems.FAN, "Unfolded fan");
        addItem(SCAItems.FAN_FOLDED, "Fan");
        addItem(SCAItems.FIRE_BRUSH, "Historian brush");

        addBlock(SCABlocks.PAPER_DRYING_RACK, "Paper drying rack");

        add("itemGroup.sino_calligraphy", "SinoCalligraphy");

        add(FilledXuanPaperItem.HOVER_AUTHOR_PREFIX, "Author: ");

        add(DrawHolder.KEY_NO_AUTHOR, "Unknown");

        add(BrushGuiScreen.KEY_SAVE, "Save");
        add(BrushGuiScreen.KEY_SAVE_SUCCEED, "Save Succeed!");
        add(BrushGuiScreen.KEY_SAVE_ERR_INK, "No enough ink");
        add(BrushGuiScreen.KEY_SAVE_ERR_PAPER, "No enough paper");
        add(BrushGuiScreen.KEY_COPY, "Left click to copy, right click to paste");
        add(BrushGuiScreen.KEY_COPIED, "Copied");
        add(BrushGuiScreen.KEY_PASTE_FAILED, "Failed to paste draw %s");
        add(BrushGuiScreen.KEY_PASTE_SUCCEED, "Paste succeed");
        add(BrushGuiScreen.KEY_OUTPUT, "Output to file");
        add(BrushGuiScreen.KEY_OUTPUT_SUCCEED, "Output succeed");
        add(BrushGuiScreen.KEY_OUTPUT_FAILED, "Output failed: %s");
        add("sinocalligraphy.gui.button.up", "Previous Color");
        add("sinocalligraphy.gui.button.down", "Next Color");
        add(BrushGuiScreen.KEY_DRAW_EMPTY, "Draw something on the paper");
        add(BrushGuiScreen.KEY_SAVING, "Saving...");

        add(FanFoldedItem.FOLDED_DESCRIPTION_LINE_1, "Legend said, TaoismDeeplake used this fan during his speech.");
        add(FanFoldedItem.FOLDED_DESCRIPTION_LINE_2, "He wandered the world without a sword in his hand.");

        add(FanItem.UNFOLDED_DESCRIPTION_LINE_1, "Opening this fan, Rivers and mountains rendered by light ink floats on it.");
        add(FanItem.UNFOLDED_DESCRIPTION_LINE_2, "Carrying this fan, there will be no fear of disappointing life. fears.");

        add(FireBrushItem.FIRE_BRUSH_DESC_KEY_1, "This is a brush that burns with flames! Record the righteous words.");
        add(FireBrushItem.FIRE_BRUSH_DESC_KEY_2, "In fact, it just dipped in red ink (?)");
        add(SCAItems.FIRE_BRUSH.get().getDescriptionId() + ".clue", "Time, time will tell us the answer.");

        add("sinocalligraphy.advancements.sca", "SinoCalligraphy");
        add("sinocalligraphy.advancements.sca.desc", "The painting that used color to depict all walks of life, while retained black and white to render the beauty of ages. (Use Chinese for better experience.)");
        add("sinocalligraphy.advancements.draw", "On a paper, eyes can be brought to life, whether they are beautiful, or ugly.");
        add("sinocalligraphy.advancements.draw.desc", "Once put a drop of ink on paper.");
        add("sinocalligraphy.advancements.ink", "One will see it stained, suddenly, yet silently.");
        add("sinocalligraphy.advancements.ink.desc", "That is it, no matter how to get some ink.");
        add("sinocalligraphy.advancements.fan", "Once holding this fan, one will be not afraid of torture and misery.");
        add("sinocalligraphy.advancements.fan.desc", "Once holding this fan, the spirit of a knightly and free man, is self-evident.");
    }
}
