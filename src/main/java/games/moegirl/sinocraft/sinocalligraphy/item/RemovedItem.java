package games.moegirl.sinocraft.sinocalligraphy.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class RemovedItem extends Item {

    public static final String HOVER_KEY = "sino_calligraphy.tooltips.deprecated";

    private final Function<ItemStack, ItemStack> convert;

    public RemovedItem(Properties pProperties, Function<ItemStack, ItemStack> convert) {
        super(pProperties);
        this.convert = convert;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TranslatableComponent(HOVER_KEY).withStyle(ChatFormatting.RED));
        pTooltipComponents.add(new TranslatableComponent(HOVER_KEY).withStyle(ChatFormatting.RED));
        pTooltipComponents.add(new TranslatableComponent(HOVER_KEY).withStyle(ChatFormatting.RED));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (stack.is(this)) {
            ItemStack newStack = convert.apply(stack);
            pPlayer.setItemInHand(pUsedHand, newStack);
            return InteractionResultHolder.pass(newStack);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
