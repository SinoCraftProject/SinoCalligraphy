package games.moegirl.sinocraft.sinocalligraphy.gui;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraftforge.common.extensions.IForgeMenuType.create;

public class SCAMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SinoCalligraphy.MODID);

    public static RegistryObject<MenuType<BrushMenu>> BRUSH = MENUS.register("chinese_brush", () -> create(BrushMenu::new));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
