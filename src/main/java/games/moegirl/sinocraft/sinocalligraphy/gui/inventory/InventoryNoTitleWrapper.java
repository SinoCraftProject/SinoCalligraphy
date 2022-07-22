package games.moegirl.sinocraft.sinocalligraphy.gui.inventory;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class InventoryNoTitleWrapper extends Inventory {
    public InventoryNoTitleWrapper(Player arg) {
        super(arg);
    }

    @Override
    public Component getName() {
        return TextComponent.EMPTY;
    }
}
