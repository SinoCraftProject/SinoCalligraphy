package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.block.SCABlocks;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class SCARecipeProvider extends RecipeProvider {
    public SCARecipeProvider(DataGenerator arg) {
        super(arg);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        shaped(SCAItems.BRUSH, Items.WHITE_WOOL)
                .pattern("S")
                .pattern("W")
                .define('S', Items.STICK)
                .define('W', Items.WHITE_WOOL)
                .save(consumer);

        shaped(SCABlocks.PAPER_DRYING_RACK, Items.STICK)
                .pattern("  S")
                .pattern(" TS")
                .pattern("S S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .save(consumer);

        // Todo: Better Xuan paper recipe.
        // Todo: Better Ink recipe.
    }

    private ShapedRecipeBuilder shaped(RegistryObject<? extends ItemLike> result, ItemLike unlockedBy) {
        return ShapedRecipeBuilder.shaped(result.get())
                .group(SinoCalligraphy.MODID)
                .unlockedBy("has_block", has(unlockedBy));
    }

    private ShapelessRecipeBuilder shapeless(ItemLike result, RegistryObject<? extends ItemLike> unlockedBy) {
        return ShapelessRecipeBuilder.shapeless(result)
                .group(SinoCalligraphy.MODID)
                .unlockedBy("has_block", has(unlockedBy.get()));
    }
}
