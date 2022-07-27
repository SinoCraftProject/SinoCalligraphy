package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.client.XuanPaperRenderer;
import games.moegirl.sinocraft.sinocalligraphy.drawing.Constants;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class FilledXuanPaperItem extends Item {
    public static final String HOVER_AUTHOR_PREFIX = SinoCalligraphy.MODID + ".hover.author.prefix";

    public FilledXuanPaperItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .setNoRepair());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel,
                                List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(stack, pLevel, pTooltipComponents, pIsAdvanced);

        var tag = stack.getOrCreateTag();
        if (!tag.contains(Constants.TAG_HOLDER)) {
            return;
        }

        var nbt = tag.getCompound(Constants.TAG_HOLDER);

        DrawHolder.parse(nbt)
                .map(DrawHolder::getAuthor)
                .map(c -> new TranslatableComponent(HOVER_AUTHOR_PREFIX).append(c))
                .ifPresent(pTooltipComponents::add);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return XuanPaperRenderer.getInstance();
            }
        });
    }
}
