package games.moegirl.sinocraft.sinocalligraphy.gui.menu;

import games.moegirl.sinocraft.sinocalligraphy.gui.container.BrushContainer;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

/**
 * Menu(Container) of Chinese brush.
 * @author qyl27
 */
public class BrushMenu extends AbstractContainerMenu {
    protected BrushContainer brushContainer;
    protected Inventory playerInventory;

    public BrushMenu(int id, Inventory playerInv) {
        super(SCAMenus.BRUSH.get(), id);
        playerInventory = playerInv;
        brushContainer = new BrushContainer();
    }

    /**
     * Add slots of the container.
     */
    protected void addSlots() {

    }

    protected void addContainerSlots() {

    }

    protected void addSlotLine() {

    }

    protected void addPlayerSlots() {

    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(SCAItems.BRUSH.get())
                || player.getOffhandItem().is(SCAItems.BRUSH.get());
    }
}
