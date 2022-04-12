package games.moegirl.sinocraft.sinocalligraphy.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SCACreativeTab extends CreativeModeTab {
    public static final CreativeModeTab CALLIGRAPHY = new SCACreativeTab();

    public SCACreativeTab() {
        super("sino_calligraphy");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(SCAItems.BRUSH.get());
    }
}
