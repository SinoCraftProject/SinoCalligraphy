package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.fluid.SCAFluids;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCAItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SinoCalligraphy.MODID);

    public static final RegistryObject<Item> BRUSH = ITEMS.register("chinese_brush", BrushItem::new);
    public static final RegistryObject<Item> FIRE_BRUSH = ITEMS.register("fire_chinese_brush", FireBrushItem::new);
    public static final RegistryObject<Item> INK = ITEMS.register("ink", () -> new Item(new Item.Properties().setNoRepair().stacksTo(64).tab(SCACreativeTab.CALLIGRAPHY)));

    public static final RegistryObject<Item> EMPTY_XUAN_PAPER = ITEMS.register("empty_xuan_paper", () -> new XuanPaperItem(XuanPaperType.WHITE));
    public static final RegistryObject<Item> EMPTY_XUAN_PAPER_RED = ITEMS.register("empty_xuan_paper_red", () -> new XuanPaperItem(XuanPaperType.RED));
    public static final RegistryObject<Item> EMPTY_XUAN_PAPER_BLACK = ITEMS.register("empty_xuan_paper_black", () -> new XuanPaperItem(XuanPaperType.BLACK));
    public static final RegistryObject<Item> FILLED_XUAN_PAPER = ITEMS.register("filled_xuan_paper", FilledXuanPaperItem::new);

    public static final RegistryObject<BucketItem> WOOD_PULP_BUCKET = ITEMS.register("wood_pulp_bucket", () -> new BucketItem(SCAFluids.WOOD_PULP, new Item.Properties().tab(SCACreativeTab.CALLIGRAPHY).setNoRepair().stacksTo(1)));

    public static final RegistryObject<Item> FAN = ITEMS.register("fan", FanItem::new);
    public static final RegistryObject<Item> FAN_FOLDED = ITEMS.register("fan_folded", FanFoldedItem::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
