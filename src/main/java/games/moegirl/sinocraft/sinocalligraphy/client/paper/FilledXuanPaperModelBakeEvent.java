package games.moegirl.sinocraft.sinocalligraphy.client.paper;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client render event subscriber.
 */
@Mod.EventBusSubscriber(modid = SinoCalligraphy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class FilledXuanPaperModelBakeEvent {
    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        var registry = event.getModelRegistry();
        ModelResourceLocation location = new ModelResourceLocation(SCAItems.XUAN_PAPER.getId(), "inventory");
        BakedModel exist_model = registry.get(location);
        if (exist_model == null) {
            throw new IllegalStateException(location + " has no existing model");
        }
        registry.put(location, new FilledXuanPaperBakedModel(exist_model));
    }
}
