package games.moegirl.sinocraft.sinocalligraphy.utility;

import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.Function;

public enum XuanPaperType {
    WHITE("white", 1, SCAItems.EMPTY_XUAN_PAPER, FastColor.ARGB32.color(0, 0, 0, 0), FastColor.ARGB32.color(255, 255, 255, 255), DrawHelper::defaultMix),
    RED("red", 2, SCAItems.EMPTY_XUAN_PAPER_RED, FastColor.ARGB32.color(0, 0, 0, 0), FastColor.ARGB32.color(255, 235, 76, 68), DrawHelper::defaultMix),
    BLACK("black", 3, SCAItems.EMPTY_XUAN_PAPER_BLACK, FastColor.ARGB32.color(0, 255, 225, 64), FastColor.ARGB32.color(255, 0, 0, 0), DrawHelper::mixWithBackgroundBlack);

    private final RegistryObject<Item> item;
    private final String typeName;
    private final int id;

    private final int foreground;
    private final int background;

    private final Function<Triple<Integer, Integer, Integer>, Integer> mix;

    private XuanPaperType(String name, int idIn, RegistryObject<Item> itemIn, int fg, int bg, Function<Triple<Integer, Integer, Integer>, Integer> mixFunction) {
        typeName = name;
        id = idIn;
        item = itemIn;

        foreground = fg;
        background = bg;

        mix = mixFunction;
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

    public int mix(int alpha) {
        return mix.apply(Triple.of(getForeground(), getBackground(), alpha));
    }
}
