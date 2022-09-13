package games.moegirl.sinocraft.sinocalligraphy.client;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.block.SCABlocks;
import games.moegirl.sinocraft.sinocalligraphy.fluid.SCAFluids;
import games.moegirl.sinocraft.sinocalligraphy.gui.BrushGuiScreen;
import games.moegirl.sinocraft.sinocalligraphy.gui.SCAMenus;
import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.gui.inventory.InventoryNoTitleWrapper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SinoCalligraphy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class SCAClientScreens {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuType<? extends BrushMenu> type = SCAMenus.BRUSH.get();
            MenuScreens.ScreenConstructor<BrushMenu, BrushGuiScreen> factory = (menu, inv, title) -> new BrushGuiScreen(menu, new InventoryNoTitleWrapper(inv.player), TextComponent.EMPTY);
            MenuScreens.register(type, factory);
        });

        ItemBlockRenderTypes.setRenderLayer(SCABlocks.WOOD_PULP_BLOCK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(SCAFluids.WOOD_PULP.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(SCAFluids.WOOD_PULP_FLOWING.get(), RenderType.translucent());
    }
}
