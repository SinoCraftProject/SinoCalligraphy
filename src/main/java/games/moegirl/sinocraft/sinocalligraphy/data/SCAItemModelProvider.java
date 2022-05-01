package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocalligraphy.item.XuanPaperItem;
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
        skipItems(SCAItems.BRUSH);
        skipItems(SCAItems.XUAN_PAPER_FILLED, SCAItems.EMPTY_XUAN_PAPER, SCAItems.XUAN_PAPER);
        generatedItem(SCAItems.XUAN_PAPER.get()).override().predicate(XuanPaperItem.HAS_DRAW, 1).model(generatedItem("xuan_paper2")).end();
        super.registerModels();
    }

    @Override
    protected ItemModelBuilder generatedItem(String name) {
        return this.withExistingParent("sinocore:" + name, GENERATED).texture("layer0", this.modLoc("item/" + name));
    }
}
