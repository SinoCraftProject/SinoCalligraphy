package games.moegirl.sinocraft.sinocalligraphy.gui.slot;

import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Input slot of the Chinese brush.
 * @author qyl27
 */
public class EmptyXuanPaperSlot extends Slot {
    /**
     * Constructor of this class.
     * @param container The inventory of the menu. It named IInventory in MCP.
     * @param index Slot in inventory index.
     * @param x X location in screen of the slot. (From left.)
     * @param y Y location in screen of the slot. (From up.)
     */
    public EmptyXuanPaperSlot(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(SCAItems.BRUSH.get());
    }
}
