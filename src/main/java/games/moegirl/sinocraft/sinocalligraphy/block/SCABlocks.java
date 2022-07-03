package games.moegirl.sinocraft.sinocalligraphy.block;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.fluid.SCAFluids;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCABlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SinoCalligraphy.MODID);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

    public static final RegistryObject<Block> PAPER_DRYING_RACK = BLOCKS.register("paper_drying_rack", PaperDryingRackBlock::new);

    public static final RegistryObject<LiquidBlock> WOOD_PULP_BLOCK = BLOCKS.register("wood_plup", () -> new LiquidBlock(SCAFluids.WOOD_PULP, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100f).noDrops()));
}
