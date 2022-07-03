package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.block.SCABlockItems;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocore.api.data.ItemModelProviderBase;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.DeferredRegister;

public class SCAItemModelProvider extends ItemModelProviderBase {
    public SCAItemModelProvider(DataGenerator generator, String modId, ExistingFileHelper exHelper, DeferredRegister<? extends Item> deferredRegister) {
        super(generator, modId, exHelper, deferredRegister);
    }

    @Override
    protected void registerItemModels() {
        skipItems.add(SCAItems.BRUSH.get());
        skipItems.add(SCAItems.INK.get());
        skipItems.add(SCAItems.WOOD_PULP_BUCKET.get());

        skipItems.add(SCABlockItems.PAPER_DRYING_RACK.get());
        namedBlockItem(SCABlockItems.PAPER_DRYING_RACK.get(), "paper_drying_rack_0");
    }

    protected void namedBlockItem(BlockItem blockItem, String name) {
        getBuilder(blockItem.getRegistryName().getPath()).parent(getModel("block/" + name));
    }

    protected ModelFile getModel(String loc) {
        return new ModelFile.UncheckedModelFile(new ResourceLocation(modid, loc));
    }


    // Fixme: qyl27: SC breaks here.
//    @Override
//    protected ItemModelBuilder generatedItem(String name) {
//        return this.withExistingParent("sinocore:" + name, GENERATED).texture("layer0", this.modLoc("item/" + name));
//    }
}

