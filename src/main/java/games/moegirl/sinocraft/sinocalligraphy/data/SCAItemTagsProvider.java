package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import games.moegirl.sinocraft.sinocore.api.data.BlockTagsProviderBase;
import games.moegirl.sinocraft.sinocore.api.data.ItemTagsProviderBase;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class SCAItemTagsProvider extends ItemTagsProviderBase {
    public SCAItemTagsProvider(DataGenerator pGenerator, BlockTagsProviderBase blockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, blockTags, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(SCAItemTags.INKS).add(SCAItems.INK.get());

        tag(SCAItemTags.FAN).add(SCAItems.FAN.get(), SCAItems.FAN_FOLDED.get());
    }
}
