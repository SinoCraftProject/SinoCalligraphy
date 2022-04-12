package games.moegirl.sinocraft.sinocalligraphy.item;

import net.minecraft.world.item.Item;

public class FilledXuanPaperItem extends Item {
    public FilledXuanPaperItem() {
        super(new Properties()
                .tab(SCACreativeTab.CALLIGRAPHY)
                .stacksTo(64)
                .setNoRepair());
    }
}
