package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.client.XuanPaperRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class FilledXuanPaper extends Item {
    public static final int SIZE = 32;
    public static final int PIXEL_COUNT = SIZE * SIZE;
    public static final String TAG_PIXELS = "pixels";
    public static final String TAG_AUTHOR = "author";
    public static final String HOVER_AUTHOR_PREFIX = SinoCalligraphy.MODID + ".hover.author.prefix";
    public static final String HOVER_AUTHOR_EMPTY = SinoCalligraphy.MODID + ".hover.author.empty";

    public FilledXuanPaper() {
        super(new Item.Properties()
                .stacksTo(1)
                .setNoRepair());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TranslatableComponent(HOVER_AUTHOR_PREFIX).append(getAuthor(pStack)));
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

    public static byte[] getDraw(ItemStack paper) {
        if (isDrawn(paper)) {
            return adjustSize(paper.getOrCreateTag().getByteArray(TAG_PIXELS));
        } else {
            return new byte[PIXEL_COUNT];
        }
    }

    public static Component getAuthor(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TAG_AUTHOR, Tag.TAG_STRING)) {
            String name = tag.getString(TAG_AUTHOR);
            MutableComponent component = Component.Serializer.fromJson(name);
            return Objects.requireNonNullElseGet(component,
                    () -> new TextComponent(name));
        }
        return new TranslatableComponent(HOVER_AUTHOR_EMPTY);
    }

    public static ItemStack draw(byte[] draw, @Nullable Component author) {
        ItemStack paper = new ItemStack(SCAItems.FILLED_XUAN_PAPER.get());
        CompoundTag tag = paper.getOrCreateTag();
        tag.putByteArray(TAG_PIXELS, adjustSize(draw));

        if (author != null) {
            tag.putString(TAG_AUTHOR, Component.Serializer.toJson(author));
        }

        return paper;
    }

    public static ItemStack draw(byte[] draw) {
        return draw(draw, null);
    }

    public static boolean isDrawn(ItemStack paper) {
        return paper.is(SCAItems.FILLED_XUAN_PAPER.get()) && paper.hasTag() && paper.getOrCreateTag().contains(TAG_PIXELS, Tag.TAG_BYTE_ARRAY);
    }

    public static byte[] adjustSize(byte[] draw) {
        byte[] b = draw;
        if (draw.length != PIXEL_COUNT) {
            b = new byte[PIXEL_COUNT];
            int len = Math.min(PIXEL_COUNT, draw.length);
            System.arraycopy(draw, 0, b, 0, len);
            if (len < PIXEL_COUNT) {
                Arrays.fill(b, len, PIXEL_COUNT, (byte) 0);
            }
        }
        return b;
    }
}
