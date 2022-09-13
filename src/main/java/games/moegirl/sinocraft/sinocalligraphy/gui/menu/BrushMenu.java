package games.moegirl.sinocraft.sinocalligraphy.gui.menu;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.data.SCAItemTags;
import games.moegirl.sinocraft.sinocalligraphy.gui.SCAMenus;
import games.moegirl.sinocraft.sinocalligraphy.gui.container.BrushContainer;
import games.moegirl.sinocraft.sinocalligraphy.gui.slot.BrushInputSlot;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.SaveFailedS2CPacket;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import games.moegirl.sinocraft.sinocore.api.utility.texture.SlotStrategy;
import games.moegirl.sinocraft.sinocore.api.utility.texture.TextureMap;
import games.moegirl.sinocraft.sinocore.gui.slot.TakeOnlySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
 *
 * @author qyl27
 */
public class BrushMenu extends AbstractContainerMenu {
    public static final ResourceLocation GUI = new ResourceLocation(SinoCalligraphy.MODID, "textures/gui/chinese_brush.png");
    public static TextureMap TEXTURE = TextureMap.of(GUI);

    protected BrushContainer brushContainer;
    protected Inventory playerInventory;
    protected int brushColor = 0;

    protected ItemStack brush;
    public GuiController gui = new GuiController();

    /**
     * Initialize BrushMenu.
     * Don't forget to initController.
     *
     * @param id        Menu Id.
     * @param playerInv Player inventory.
     * @param brushIn   Brush item.
     */
    public BrushMenu(int id, Inventory playerInv, ItemStack brushIn) {
        super(SCAMenus.BRUSH.get(), id);
        playerInventory = playerInv;
        brushContainer = new BrushContainer(this);

        brush = brushIn;

        TEXTURE.placeSlot(brushContainer, "paper", BrushContainer.XUAN_PAPER_SLOT, this::addSlot,
                (container, slot, x, y) -> new BrushInputSlot(brushContainer, slot, x, y, SCAItemTags.PAPERS, this) {
                    @Override
                    public void setChanged() {
                        super.setChanged();
                        gui.setPaperType(XuanPaperType.of(getItem()));
                    }
                });
        TEXTURE.placeSlot(brushContainer, "ink", BrushContainer.INK_SLOT, this::addSlot,
                (container, slot, x, y) ->  new BrushInputSlot(brushContainer, slot, x, y, SCAItemTags.INKS, this));
        TEXTURE.placeSlot(brushContainer, "result", BrushContainer.FILLED_XUAN_PAPER_SLOT, this::addSlot,
                (container, slot, x, y) -> new TakeOnlySlot(container, slot, x, y) {
                    // qyl27: There are some **** code.
                    @Override
                    public @NotNull ItemStack safeTake(int p_150648_, int p_150649_, @NotNull Player player) {
                        if (brushContainer.canPaint()) {
                            if (brushContainer.isDrawEmpty()) {
                                return ItemStack.EMPTY;
                            }

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
        TEXTURE.placeSlots(playerInventory, "inventory", 9, this::addSlot, SlotStrategy.noLimit());
        TEXTURE.placeSlots(playerInventory, "selection", 0, this::addSlot, SlotStrategy.noLimit());
    }

    public BrushMenu(int id, Inventory playerInv, FriendlyByteBuf data) {
        this(id, playerInv, data.readItem());
    }

    /**
     * Invoke this before use gui.
     */
    public void setController(GuiController controller) {
        gui = controller;
    }

    public GuiController getController() {
        return gui;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(SCAItemTags.BRUSHES)
                || player.getOffhandItem().is(SCAItemTags.BRUSHES);
    }

    /**
     * qyl27: Don't forget to rewrite removed method to put back items.
     *
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
        brushColor = Mth.clamp(color, 0, 15);
    }

    public static class GuiController {
        public void handleSaveResult(SaveFailedS2CPacket.Reason code, int button) {
        }

        public void setCanvasEnable(boolean isEnable) {
        }

        public void clearCanvas() {
        }

        public void onTake(Player player, ItemStack stack) {
        }

        public void setPaperType(XuanPaperType type) {
        }

        public boolean isEmpty() {
            return true;
        }
    }
}
