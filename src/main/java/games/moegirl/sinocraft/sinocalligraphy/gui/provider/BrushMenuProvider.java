package games.moegirl.sinocraft.sinocalligraphy.gui.provider;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class BrushMenuProvider implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("sinocraft.calligraphy.brush.menu");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new BrushMenu(id, inv);
    }
}
