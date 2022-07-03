package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.block.PaperDryingRackBlock;
import games.moegirl.sinocraft.sinocalligraphy.block.SCABlocks;
import games.moegirl.sinocraft.sinocore.api.data.BlockStateProviderBase;
import games.moegirl.sinocraft.sinocore.api.data.base.WarnBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.DeferredRegister;

public class SCABlockStateProvider extends BlockStateProviderBase {
    public SCABlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper, DeferredRegister<Block> registry) {
        super(gen, modid, exFileHelper, registry);
    }

    @Override
    protected void registerStatesAndModels() {
        addPaperDryingRack();
    }

    private void addPaperDryingRack() {
        MultiPartBlockStateBuilder paperDryingRackBuilder = getMultipartBuilder(SCABlocks.PAPER_DRYING_RACK.get());
        for (int i = 0; i <= 4; i++) {
            Direction direction = Direction.NORTH;
            for (int j = 0; j < 4; j++) {
                paperDryingRackBuilder.part().modelFile(models().getExistingFile(modLoc("block/paper_drying_rack_" + i)))
                        .rotationY(90 * j)
                        .addModel()
                        .condition(PaperDryingRackBlock.PROCESS, i)
                        .condition(PaperDryingRackBlock.FACING, direction)
                        .end();
                direction = direction.getClockWise();
            }
        }
    }
}
