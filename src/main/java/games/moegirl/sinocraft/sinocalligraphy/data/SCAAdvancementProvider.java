package games.moegirl.sinocraft.sinocalligraphy.data;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.item.SCAItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class SCAAdvancementProvider extends AdvancementProvider {
    public SCAAdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        var root = Advancement.Builder.advancement()
                .display(SCAItems.BRUSH.get(),
                        new TranslatableComponent("sinocalligraphy.advancements.sca"),
                        new TranslatableComponent("sinocalligraphy.advancements.sca.desc"),
                        new ResourceLocation(SinoCalligraphy.MODID, "textures/advancement/white_marble.png"),
                        FrameType.TASK, false, true, false)
                .addCriterion("login", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                .rewards(AdvancementRewards.EMPTY)
                .save(consumer, "sinocalligraphy:root");

        Advancement.Builder.advancement()
                .display(SCAItems.INK.get(),
                        new TranslatableComponent("sinocalligraphy.advancements.ink"),
                        new TranslatableComponent("sinocalligraphy.advancements.ink.desc"),
                        new ResourceLocation(SinoCalligraphy.MODID, "textures/advancement/white_marble.png"),
                        FrameType.TASK, false, true, false)
                .addCriterion("ink", new InventoryChangeTrigger.TriggerInstance(
                        EntityPredicate.Composite.ANY,
                        MinMaxBounds.Ints.ANY,
                        MinMaxBounds.Ints.ANY,
                        MinMaxBounds.Ints.ANY,
                        new ItemPredicate[]{ItemPredicate.Builder.item().of(SCAItems.INK.get()).build()}))
                .parent(root)
                .rewards(AdvancementRewards.EMPTY)
                .save(consumer, "sinocalligraphy:ink");

        Advancement.Builder.advancement()
                .display(SCAItems.FILLED_XUAN_PAPER.get(),
                        new TranslatableComponent("sinocalligraphy.advancements.draw"),
                        new TranslatableComponent("sinocalligraphy.advancements.draw.desc"),
                        new ResourceLocation(SinoCalligraphy.MODID, "textures/advancement/white_marble.png"),
                        FrameType.TASK, false, true, false)
                .addCriterion("draw", new InventoryChangeTrigger.TriggerInstance(
                        EntityPredicate.Composite.ANY,
                        MinMaxBounds.Ints.ANY,
                        MinMaxBounds.Ints.ANY,
                        MinMaxBounds.Ints.ANY,
                        new ItemPredicate[]{ItemPredicate.Builder.item().of(SCAItems.FILLED_XUAN_PAPER.get()).build()}))
                .parent(root)
                .rewards(AdvancementRewards.EMPTY)
                .save(consumer, "sinocalligraphy:draw");

        Advancement.Builder.advancement()
                .display(SCAItems.FAN.get(),
                        new TranslatableComponent("sinocalligraphy.advancements.fan"),
                        new TranslatableComponent("sinocalligraphy.advancements.fan.desc"),
                        new ResourceLocation(SinoCalligraphy.MODID, "textures/advancement/white_marble.png"),
                        FrameType.TASK, false, true, true)
                .addCriterion("fan", new InventoryChangeTrigger.TriggerInstance(
                        EntityPredicate.Composite.ANY,
                        MinMaxBounds.Ints.ANY,
                        MinMaxBounds.Ints.ANY,
                        MinMaxBounds.Ints.ANY,
                        new ItemPredicate[]{ItemPredicate.Builder.item().of(SCAItems.FAN.get()).build()}))
                .parent(root)
                .rewards(AdvancementRewards.EMPTY)
                .save(consumer, "sinocalligraphy:fan");
    }
}
