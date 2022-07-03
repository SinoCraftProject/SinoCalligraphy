package games.moegirl.sinocraft.sinocalligraphy.fluid;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.block.SCABlocks;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCAFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, SinoCalligraphy.MODID);

    public static final RegistryObject<FlowingFluid> WOOD_PULP = FLUIDS.register("wood_plup", () -> new ForgeFlowingFluid.Source(SCAFluids.WOOD_PULP_PROPERTY));
    public static final RegistryObject<FlowingFluid> WOOD_PULP_FLOWING = FLUIDS.register("wood_plup_flowing", () -> new ForgeFlowingFluid.Flowing(SCAFluids.WOOD_PULP_PROPERTY));

    public static final ForgeFlowingFluid.Properties WOOD_PULP_PROPERTY = new ForgeFlowingFluid.Properties(
            WOOD_PULP, WOOD_PULP_FLOWING,
            FluidAttributes.builder(
                    new ResourceLocation(SinoCalligraphy.MODID, "fluid/water_still"),
                    new ResourceLocation(SinoCalligraphy.MODID, "fluid/water_flow"))
                    .density(10).luminosity(0).viscosity(3).sound(SoundEvents.WATER_AMBIENT)
                    .overlay(new ResourceLocation(SinoCalligraphy.MODID, "fluid/water_overlay"))
                    .color(0xFFBC8129))
            .block(SCABlocks.WOOD_PULP_BLOCK)
            .bucket(SCAItems.WOOD_PULP_BUCKET);


    public static void register(IEventBus bus) {
        FLUIDS.register(bus);
    }
}
