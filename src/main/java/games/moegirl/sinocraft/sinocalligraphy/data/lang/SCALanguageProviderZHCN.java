package games.moegirl.sinocraft.sinocalligraphy.data.lang;

import games.moegirl.sinocraft.sinocalligraphy.block.SCABlocks;
import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
import games.moegirl.sinocraft.sinocalligraphy.item.*;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
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
        addItem(SCAItems.WOOD_PULP_BUCKET, "木浆桶");
        addItem(SCAItems.FAN, "展开的折扇");
        addItem(SCAItems.FAN_FOLDED, "折扇");
        addItem(SCAItems.FIRE_BRUSH, "史官笔");

        addBlock(SCABlocks.PAPER_DRYING_RACK, "晾纸架");

        add("itemGroup.sino_calligraphy", "华夏丹青");

        add(FilledXuanPaperItem.HOVER_AUTHOR_PREFIX, "作者：");
        add(DrawHolder.KEY_NO_AUTHOR, "佚名");

        add(BrushGuiScreen.KEY_SAVE, "保存");
        add(BrushGuiScreen.KEY_SAVE_SUCCEED, "保存成功!");
        add(BrushGuiScreen.KEY_SAVE_ERR_INK, "墨汁不足");
        add(BrushGuiScreen.KEY_SAVE_ERR_PAPER, "宣纸不足");
        add(BrushGuiScreen.KEY_COPY, "左键复制，右键粘贴");
        add(BrushGuiScreen.KEY_COPIED, "已复制");
        add(BrushGuiScreen.KEY_PASTE_FAILED, "粘贴失败 %s");
        add(BrushGuiScreen.KEY_PASTE_SUCCEED, "粘贴成功");
        add(BrushGuiScreen.KEY_OUTPUT, "导出到文件");
        add(BrushGuiScreen.KEY_OUTPUT_SUCCEED, "导出成功");
        add(BrushGuiScreen.KEY_OUTPUT_FAILED, "导出失败: %s");
        add("sinocalligraphy.gui.button.up", "上一个颜色");
        add("sinocalligraphy.gui.button.down", "下一个颜色");
        add(BrushGuiScreen.KEY_DRAW_EMPTY, "在纸上画些什么吧");
        add(BrushGuiScreen.KEY_SAVING, "保存中");

        add(FanFoldedItem.FOLDED_DESCRIPTION_LINE_1, "传说，道家深湖在演讲时就用过这把扇子");
        add(FanFoldedItem.FOLDED_DESCRIPTION_LINE_2, "他浪迹天地，无长剑在手");
        add(SCAItems.FAN_FOLDED.get().getDescriptionId() + ".clue", "去墨染的山河中寻找说书人吧。");

        add(FanItem.UNFOLDED_DESCRIPTION_LINE_1, "开此扇，上有淡墨河山");
        add(FanItem.UNFOLDED_DESCRIPTION_LINE_2, "携此扇，无惧此生风雨");

        add(FireBrushItem.FIRE_BRUSH_DESC_KEY_1, "这是燃烧着火焰的笔！记录着大义微言。");
        add(FireBrushItem.FIRE_BRUSH_DESC_KEY_2, "其实就是蘸了红墨水（？）");
        add(SCAItems.FIRE_BRUSH.get().getDescriptionId() + ".clue", "时间，时间会告诉我们答案。");

        add("sinocalligraphy.advancements.sca", "华夏丹青");
        add("sinocalligraphy.advancements.sca.desc", "丹青描绘浮生，云墨书尽芳华");
        add("sinocalligraphy.advancements.draw", "纸上描眉目，不辨妍或媸");
        add("sinocalligraphy.advancements.draw.desc", "在纸上着墨");
        add("sinocalligraphy.advancements.ink", "墨染，蓦然，默然");
        add("sinocalligraphy.advancements.ink.desc", "不论用什么方法获得一些墨汁");
        add("sinocalligraphy.advancements.fan", "此展折扇，此任平生不惧风与雪");
        add("sinocalligraphy.advancements.fan.desc", "此身洒脱，此间侠义何需他人说");
    }
}
