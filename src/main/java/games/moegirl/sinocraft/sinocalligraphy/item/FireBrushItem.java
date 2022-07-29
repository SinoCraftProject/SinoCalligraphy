package games.moegirl.sinocraft.sinocalligraphy.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FireBrushItem extends BrushItem {
    public static final String FIRE_BRUSH_DESC_KEY_1 = "sinocalligraphy.fire_brush.desc.1";
    public static final String FIRE_BRUSH_DESC_KEY_2 = "sinocalligraphy.fire_brush.desc.2";

    public FireBrushItem() {
        super(new Properties()
                .tab(SCACreativeTab.CALLIGRAPHY)
                .fireResistant()
                .setNoRepair());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        tooltipComponents.add(new TranslatableComponent(FIRE_BRUSH_DESC_KEY_1).withStyle(ChatFormatting.GRAY));

        if (isAdvanced == TooltipFlag.Default.ADVANCED) {
            tooltipComponents.add(new TranslatableComponent(FIRE_BRUSH_DESC_KEY_2).withStyle(ChatFormatting.GRAY));
        }
    }
}
