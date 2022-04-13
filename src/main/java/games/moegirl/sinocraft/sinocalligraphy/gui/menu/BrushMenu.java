package games.moegirl.sinocraft.sinocalligraphy.gui.menu;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.gui.SCAMenus;
import games.moegirl.sinocraft.sinocalligraphy.gui.container.BrushContainer;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocore.gui.slot.AcceptSpecialSlot;
import games.moegirl.sinocraft.sinocore.gui.slot.TakeOnlySlot;
import games.moegirl.sinocraft.sinocore.utility.SlotHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Menu(Container) of Chinese brush.
 * @author qyl27
 */
public class BrushMenu extends AbstractContainerMenu {
    protected BrushContainer brushContainer;
    protected Inventory playerInventory;
    protected int brushColor = 0;

    public BrushMenu(int id, Inventory playerInv) {
        super(SCAMenus.BRUSH.get(), id);
        playerInventory = playerInv;
        brushContainer = new BrushContainer(this);

        addSlots();
    }

    /**
     * Add slots of the container.
     */
    protected void addSlots() {
        addSlot(new AcceptSpecialSlot(brushContainer, BrushContainer.EMPTY_XUAN_PAPER_SLOT, 14, 23, SCAItems.EMPTY_XUAN_PAPER.get()));
        addSlot(new AcceptSpecialSlot(brushContainer, BrushContainer.INK_SLOT, 14, 66, ItemTags.create(new ResourceLocation(SinoCalligraphy.MODID, "ink"))));
        addSlot(new TakeOnlySlot(brushContainer, BrushContainer.FILLED_XUAN_PAPER_SLOT, 14, 203) {
            // Todo: qyl27: Test it.
            @Override
            public @NotNull ItemStack safeTake(int p_150648_, int p_150649_, @NotNull Player player) {
                if (brushContainer.canPaint()) {
                    return super.safeTake(p_150648_, p_150649_, player);
                } else {
                    return ItemStack.EMPTY;
                }
            }
        });

        SlotHelper.addPlayerInventory(this, playerInventory, 45, 155, 18, 18);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(SCAItems.BRUSH.get())
                || player.getOffhandItem().is(SCAItems.BRUSH.get());
    }

    @Override
    protected void clearContainer(Player player, Container container) {
        brushContainer.clearAll(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;

        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();

            result = stackInSlot.copy();
            if (index < 3) {
                if (!moveItemStackTo(stackInSlot, 3, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stackInSlot, 0, 3, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }

    public void increaseBrushColor() {
        brushColor = Math.min(15, brushColor + 1);
    }

    public void decreaseBrushColor() {
        brushColor = Math.max(0, brushColor - 1);
    }

    public ItemStack getPaper() {
        return brushContainer.getItem(BrushContainer.EMPTY_XUAN_PAPER_SLOT);
    }

    public ItemStack getInk() {
        return brushContainer.getItem(BrushContainer.INK_SLOT);
    }

    public ItemStack getFilled() {
        return brushContainer.getItem(BrushContainer.FILLED_XUAN_PAPER_SLOT);
    }

    public void setFilled(ItemStack output) {
        brushContainer.setItem(BrushContainer.FILLED_XUAN_PAPER_SLOT, output);
    }

    public int getColor() {
        return brushColor;
    }
}
