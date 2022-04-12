package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocore.SinoCore;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCAItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SinoCore.MODID);

    public static final RegistryObject<Item> BRUSH = ITEMS.register("chinese_brush", () -> new BrushItem());
    public static final RegistryObject<Item> XUAN_PAPER = ITEMS.register("xuan_paper", () -> new FilledXuanPaperItem());

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}