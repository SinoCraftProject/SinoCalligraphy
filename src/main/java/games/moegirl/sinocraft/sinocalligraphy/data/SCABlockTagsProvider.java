package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocore.api.data.BlockTagsProviderBase;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class SCABlockTagsProvider extends BlockTagsProviderBase {
    public SCABlockTagsProvider(DataGenerator pGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper, String mainModIdIn) {
        super(pGenerator, modId, existingFileHelper, mainModIdIn);
    }

    @Override
    protected void addTags() {

    }
}
