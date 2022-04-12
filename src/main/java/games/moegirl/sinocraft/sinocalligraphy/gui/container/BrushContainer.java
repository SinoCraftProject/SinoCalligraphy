package games.moegirl.sinocraft.sinocalligraphy.gui.container;

import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Brush container(inventory).
 * @author qyl27
 */
public class BrushContainer implements Container {
    public static final int CONTAINER_SIZE = 3;
    public static final int EMPTY_XUAN_PAPER_SLOT = 0;
    public static final int INK_SLOT = 1;
    public static final int FILLED_XUAN_PAPER_SLOT = 2;

    public NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
    }

    /**
     * Mark dirty method.
     */
    @Override
    public void setChanged() {
        // qyl27: Keep it empty.
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(SCAItems.BRUSH.get())
                || player.getOffhandItem().is(SCAItems.BRUSH.get());
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    public boolean hasPaper() {
        return !getItem(EMPTY_XUAN_PAPER_SLOT).isEmpty();
    }

    public boolean hasInk() {
        return !getItem(INK_SLOT).isEmpty();
    }

    public void usePaper() {
        removeItem(EMPTY_XUAN_PAPER_SLOT, 1);
    }

    public void useInk() {
        removeItem(INK_SLOT, 1);
    }

    public void fillPaper() {

    }

    public void paint() {
        if (hasPaper() && hasInk()) {
            usePaper();
            useInk();
            fillPaper();
        }
    }
}
