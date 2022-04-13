package games.moegirl.sinocraft.sinocalligraphy.gui;

import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocore.SinoCore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCAMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SinoCore.MODID);

    public static RegistryObject<MenuType<BrushMenu>> BRUSH = MENUS.register("chinese_brush", () ->
            IForgeMenuType.create((int windowId, Inventory inv, FriendlyByteBuf data) ->
                    new BrushMenu(windowId, inv)));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
