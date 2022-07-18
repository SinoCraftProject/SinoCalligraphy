package games.moegirl.sinocraft.sinocalligraphy.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FanFoldedItem extends Item implements Vanishable {
    public static final String FOLDED_DESCRIPTION_LINE_1 = "sinocalligraphy.fan_folded.desc.1";
    public static final String FOLDED_DESCRIPTION_LINE_2 = "sinocalligraphy.fan_folded.desc.2";

    protected double damage = 0.5;
    protected double speed = -1.6;
    protected final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public FanFoldedItem() {
        super(new Item.Properties()
                .setNoRepair()
//                .defaultDurability(256)       // Fixme: qyl27: Why it not working?
                .tab(SCACreativeTab.CALLIGRAPHY));

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", getDamage(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", getSpeed(), AttributeModifier.Operation.ADDITION));
        defaultModifiers = builder.build();
    }

    public double getDamage() {
        return damage;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, arg -> arg.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (!player.getCooldowns().isOnCooldown(this)) {

            var newStack = new ItemStack(SCAItems.FAN.get());
            newStack.setTag(stack.getTag());
            player.getCooldowns().addCooldown(SCAItems.FAN.get(), 100);

            return InteractionResultHolder.success(newStack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltip, isAdvanced);

        if (stack.is(SCAItems.FAN_FOLDED.get())) {
            tooltip.add(new TranslatableComponent(FOLDED_DESCRIPTION_LINE_1).withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent(FOLDED_DESCRIPTION_LINE_2).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }
}
