package games.moegirl.sinocraft.sinocalligraphy.gui.slot;

import games.moegirl.sinocraft.sinocalligraphy.gui.container.BrushContainer;
import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem;
import games.moegirl.sinocraft.sinocore.gui.slot.AcceptSpecialSlot;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class BrushInputSlot extends AcceptSpecialSlot {

    private final BrushContainer container;
    private final BrushMenu menu;

    public BrushInputSlot(BrushContainer container, int index, int x, int y, Supplier<? extends ItemLike> itemIn, BrushMenu menu) {
        super(container, index, x, y, itemIn.get());
        this.container = container;
        this.menu = menu;
    }

    public BrushInputSlot(BrushContainer container, int index, int x, int y, TagKey<Item> tag, BrushMenu menu) {
        super(container, index, x, y, tag);
        this.container = container;
        this.menu = menu;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        menu.getController().setCanvasEnable(container.canPaint());
    }
}
