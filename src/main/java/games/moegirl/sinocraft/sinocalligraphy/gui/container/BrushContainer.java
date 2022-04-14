package games.moegirl.sinocraft.sinocalligraphy.gui.container;

import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocore.SinoCore;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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

    public AbstractContainerMenu menu;

    public NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    public BrushContainer(AbstractContainerMenu menuIn) {
        menu = menuIn;
    }

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
        menu.broadcastChanges();

        if (getPaper().isEmpty() || getInk().isEmpty()) {
            removeItemNoUpdate(FILLED_XUAN_PAPER_SLOT);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(SCAItems.BRUSH.get())
                || player.getOffhandItem().is(SCAItems.BRUSH.get());
    }

    @Override
    public void clearContent() {

    }

    protected ItemStack getPaper() {
        return getItem(EMPTY_XUAN_PAPER_SLOT);
    }

    protected ItemStack getInk() {
        return getItem(INK_SLOT);
    }

    protected boolean hasPaper() {
        return !getPaper().isEmpty();
    }

    protected boolean hasInk() {
        return !getInk().isEmpty();
    }

    // qyl27: We trust the stack in slot is Xuan Paper.
    public void usePaper() {
        removeItem(EMPTY_XUAN_PAPER_SLOT, 1);
        setChanged();
    }

    // qyl27: We trust the stack in slot is ink.
    public void useInk() {
        removeItem(INK_SLOT, 1);
        setChanged();
    }

    protected void fillPaper() {
        // Todo: qyl27.
        setChanged();
    }

    public boolean canPaint() {
        return hasPaper() && hasInk();
    }

    public ItemStack paint() {
        // qyl27: I made a mess.
        if (canPaint()) {
            usePaper();
            useInk();
            fillPaper();
        }

        setChanged();

        return removeItemNoUpdate(FILLED_XUAN_PAPER_SLOT);
    }

    public void dropAll(Player player) {
        if (!player.isAlive()
                || (player instanceof ServerPlayer && ((ServerPlayer) player).hasDisconnected())) {
            for (int j = 0; j < CONTAINER_SIZE - 1; ++j) {  // qyl27: No drop output slot.
                player.drop(removeItemNoUpdate(j), false);
            }
        } else {
            for (int i = 0; i < CONTAINER_SIZE - 1; ++i) {  // qyl27: No drop output slot.
                player.getInventory().placeItemBackInInventory(removeItemNoUpdate(i));
            }
        }
    }

    public boolean isPaper(ItemStack stack) {
        return stack.is(SCAItems.EMPTY_XUAN_PAPER.get());
    }

    public boolean isInk(ItemStack stack) {
        return stack.is(ItemTags.create(new ResourceLocation(SinoCore.MODID, "ink")));
    }

    /**
     * Quick add a stack to paper slot.
     * @param stack The stack to add to paper slot.
     * @return Remain stack.
     */
    public ItemStack quickAddPaper(ItemStack stack) {
        // Todo: qyl27: Waiting for test.

        ItemStack result = stack.copy();

        if (isPaper(stack)) {
            if (getPaper().is(stack.getItem())) {
                if (getPaper().isStackable()) {
                    if (getPaper().isEmpty()) {
                        setItem(EMPTY_XUAN_PAPER_SLOT, stack);
                        result = ItemStack.EMPTY;
                    } else {
                        var sumCount = getPaper().getCount() + stack.getCount();
                        var remainCount = getPaper().getMaxStackSize() - getPaper().getCount();
                        if (sumCount > getPaper().getMaxStackSize()) {
                            result.setCount(stack.getCount() - remainCount);
                            getPaper().setCount(getPaper().getMaxStackSize());
                        } else {
                            getPaper().setCount(getPaper().getCount() + stack.getCount());
                            result = ItemStack.EMPTY;
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Quick add a stack to ink slot.
     * @param stack The stack to add to ink slot.
     * @return Remain stack.
     */
    public ItemStack quickAddInk(ItemStack stack) {
        // Todo: qyl27: Waiting for test.

        ItemStack result = stack.copy();

        if (isInk(stack)) {
            if (getInk().is(stack.getItem())) {
                if (getInk().isStackable()) {
                    if (getInk().isEmpty()) {
                        setItem(INK_SLOT, stack);
                        result = ItemStack.EMPTY;
                    } else {
                        var sumCount = getInk().getCount() + stack.getCount();
                        var remainCount = getInk().getMaxStackSize() - getInk().getCount();
                        if (sumCount > getInk().getMaxStackSize()) {
                            result.setCount(stack.getCount() - remainCount);
                            getInk().setCount(getInk().getMaxStackSize());
                        } else {
                            getInk().setCount(getInk().getCount() + stack.getCount());
                            result = ItemStack.EMPTY;
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void stopOpen(Player player) {
        dropAll(player);
    }
}
