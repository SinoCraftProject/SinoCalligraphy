package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocore.api.data.ItemModelProviderBase;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.DeferredRegister;

public class SCAItemModelProvider extends ItemModelProviderBase {
    public SCAItemModelProvider(DataGenerator generator, String modId, ExistingFileHelper exHelper, DeferredRegister<? extends Item> deferredRegister) {
        super(generator, modId, exHelper, deferredRegister);
    }

    @Override
    protected void registerModels() {
        skipItems(SCAItems.BRUSH.get());
        skipItems(SCAItems.INK.get());

        super.registerModels();
    }

    // Fixme: qyl27: SC breaks here.
//    @Override
//    protected ItemModelBuilder generatedItem(String name) {
//        return this.withExistingParent("sinocore:" + name, GENERATED).texture("layer0", this.modLoc("item/" + name));
//    }
}

