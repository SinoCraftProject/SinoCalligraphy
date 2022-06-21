package games.moegirl.sinocraft.sinocalligraphy.gui.menu;

import games.moegirl.sinocraft.sinocalligraphy.data.SCAItemTags;
import games.moegirl.sinocraft.sinocalligraphy.gui.SCAMenus;
import games.moegirl.sinocraft.sinocalligraphy.gui.container.BrushContainer;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.SaveFailedS2CPacket;
import games.moegirl.sinocraft.sinocore.gui.slot.AcceptSpecialSlot;
import games.moegirl.sinocraft.sinocore.gui.slot.TakeOnlySlot;
import games.moegirl.sinocraft.sinocore.utility.SlotHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
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
public class BrushMenuBak extends AbstractContainerMenu {
    protected BrushContainer brushContainer;
    protected Inventory playerInventory;
    protected int brushColor = 0;

    protected ItemStack brush;
    public GuiController gui;

    /**
     * Initialize BrushMenu.
     * Don't forget to initController.
     * @param id Menu Id.
     * @param playerInv Player inventory.
     * @param brushIn Brush item.
     */
    public BrushMenuBak(int id, Inventory playerInv, ItemStack brushIn) {
        super(SCAMenus.BRUSH.get(), id);
        playerInventory = playerInv;
        brushContainer = new BrushContainer(this);

        brush = brushIn;

        addSlots();
    }

    public BrushMenuBak(int id, Inventory playerInv, FriendlyByteBuf data) {
        this(id, playerInv, data.readItem());
    }

    /**
     * Invoke this before use gui.
     * @param controller
     */
    public void setController(GuiController controller) {
        gui = controller;
    }

    /**
     * Add slots of the container.
     */
    protected void addSlots() {
        addSlot(new AcceptSpecialSlot(brushContainer, BrushContainer.XUAN_PAPER_SLOT, 14, 23, SCAItems.EMPTY_XUAN_PAPER.get()) {
            @Override
            public void setChanged() {
                super.setChanged();
                gui.setCanvasEnable(brushContainer.canPaint());
            }
        });
        addSlot(new AcceptSpecialSlot(brushContainer, BrushContainer.INK_SLOT, 14, 66, SCAItemTags.INKS) {
            @Override
            public void setChanged() {
                super.setChanged();
                gui.setCanvasEnable(brushContainer.canPaint());
            }
        });
        addSlot(new TakeOnlySlot(brushContainer, BrushContainer.FILLED_XUAN_PAPER_SLOT, 14, 203) {
            // qyl27: There are some **** code.
            @Override
            public @NotNull ItemStack safeTake(int p_150648_, int p_150649_, @NotNull Player player) {
                if (brushContainer.canPaint()) {
                    return super.safeTake(p_150648_, p_150649_, player);
                } else {
                    return ItemStack.EMPTY;
                }
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                gui.onTake(player, stack);

                brushContainer.paint();
                brush.setDamageValue(brush.getDamageValue() + 1);
                gui.clearCanvas();
                super.onTake(player, stack);
            }
        });

        SlotHelper.addPlayerInventory(this, playerInventory, 45, 155, 18, 18);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(SCAItems.BRUSH.get())
                || player.getOffhandItem().is(SCAItems.BRUSH.get());
    }

    /**
     * qyl27: Don't forget to rewrite removed method to put back items.
     * @param player Player entity.
     */
    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);  // qyl27: Keep the brush.
        brushContainer.dropAll(player);
    }

    @Override
    protected void clearContainer(@NotNull Player player, @NotNull Container container) {
        brushContainer.dropAll(player);
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
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
        brushColor = Math.min(16, brushColor + 1);
    }

    public void decreaseBrushColor() {
        brushColor = Math.max(0, brushColor - 1);
    }

    public ItemStack getPaper() {
        return brushContainer.getItem(BrushContainer.XUAN_PAPER_SLOT);
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

    public void setColor(int color) {
        brushColor = Mth.clamp(color, 0, 16);
    }

    public static class GuiController {
        public void handleSaveResult(SaveFailedS2CPacket.Reason code) {
        }

        public void setCanvasEnable(boolean isEnable) {
        }

        public void clearCanvas() {
        }

        public void onTake(Player player, ItemStack stack) {
        }
    }
}
