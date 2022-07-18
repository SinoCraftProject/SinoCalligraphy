package games.moegirl.sinocraft.sinocalligraphy.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FanItem extends FanFoldedItem {
    public static final String UNFOLDED_DESCRIPTION_LINE_1 = "sinocalligraphy.fan.desc.1";
    public static final String UNFOLDED_DESCRIPTION_LINE_2 = "sinocalligraphy.fan.desc.2";

    public FanItem() {
        super();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (!player.getCooldowns().isOnCooldown(this)) {

            var newStack = new ItemStack(SCAItems.FAN_FOLDED.get());
            newStack.setTag(stack.getTag());
            player.getCooldowns().addCooldown(SCAItems.FAN_FOLDED.get(), 100);

            return InteractionResultHolder.success(newStack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public double getDamage() {
        return 2.5;
    }

    @Override
    public double getSpeed() {
        return -2;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltip, isAdvanced);

        if (stack.is(SCAItems.FAN.get())) {
            tooltip.add(new TranslatableComponent(UNFOLDED_DESCRIPTION_LINE_1).withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent(UNFOLDED_DESCRIPTION_LINE_2).withStyle(ChatFormatting.GRAY));
        }
    }
}
