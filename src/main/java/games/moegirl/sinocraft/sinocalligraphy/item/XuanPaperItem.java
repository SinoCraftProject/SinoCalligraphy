package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import net.minecraft.world.item.Item;

public class XuanPaperItem extends Item {
    private XuanPaperType type;

    public XuanPaperItem(XuanPaperType typeIn) {
        super(new Properties()
                .stacksTo(64)
                .setNoRepair()
                .tab(SCACreativeTab.CALLIGRAPHY));

        type = typeIn;
    }

    public XuanPaperType getType() {
        return type;
    }
}
