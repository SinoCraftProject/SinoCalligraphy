package games.moegirl.sinocraft.sinocalligraphy.utility;

import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public enum XuanPaperType {
    WHITE("white", 1, SCAItems.EMPTY_XUAN_PAPER, FastColor.ARGB32.color(0, 0, 0, 0), FastColor.ARGB32.color(255, 255, 255, 255)),
    RED("red", 2, SCAItems.EMPTY_XUAN_PAPER_RED, FastColor.ARGB32.color(0, 0, 0, 0), FastColor.ARGB32.color(255, 235, 76, 68)),
    BLACK("black", 3, SCAItems.EMPTY_XUAN_PAPER_BLACK, FastColor.ARGB32.color(0, 255, 215, 0), FastColor.ARGB32.color(255, 0, 0, 0));

    private RegistryObject<Item> item;
    private final String typeName;
    private final int id;

    private final int foreground;
    private final int background;

    private XuanPaperType(String name, int idIn, RegistryObject<Item> itemIn, int fg, int bg) {
        typeName = name;
        id = idIn;
        item = itemIn;

        foreground = fg;
        background = bg;
    }

    public static XuanPaperType of(int typeId) {
        for (var type : values()) {
            if (type.getId() == typeId) {
                return type;
            }
        }

        return WHITE;
    }

    public String getName() {
        return typeName;
    }

    public int getId() {
        return id;
    }

    public Item getItem() {
        return item.get();
    }

    public static XuanPaperType of(String name) {
        for (var type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return WHITE;
    }

    public static XuanPaperType of(ItemStack stack) {
        for (var type : values()) {
            if (stack.is(type.getItem())) {
                return type;
            }
        }

        return WHITE;
    }

    public int getBackground() {
        return background;
    }

    public int getForeground() {
        return foreground;
    }
}
