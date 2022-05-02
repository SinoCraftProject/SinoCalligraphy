package games.moegirl.sinocraft.sinocalligraphy.client;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static net.minecraft.client.renderer.block.model.ItemTransforms.TransformType.*;

/**
 * Client render event subscriber.
 */
@Mod.EventBusSubscriber(modid = SinoCalligraphy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class XuanPaperModelBakeEvent {

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        var registry = event.getModelRegistry();
        ModelResourceLocation location = new ModelResourceLocation(SCAItems.FILLED_XUAN_PAPER.getId(), "inventory");
        BakedModel model = Objects.requireNonNull(registry.get(location), location + " has no existing model");
        registry.put(location, new ReplacedModel(model, FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND, FIXED));
    }
}
