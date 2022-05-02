package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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

public class XuanPaperItem extends Item {

    public static final int SIZE = 32;
    public static final int PIXEL_COUNT = SIZE * SIZE;
    public static final String TAG_PIXELS = "pixels";
    public static final String TAG_AUTHOR = "author";
    public static final String HOVER_AUTHOR = SinoCalligraphy.MODID + ".hover.author";
    public static final String HOVER_AUTHOR_ERROR = SinoCalligraphy.MODID + ".hover.author.unknown";
    public static final String HOVER_AUTHOR_EMPTY = SinoCalligraphy.MODID + ".hover.author.empty";

    private final boolean empty;

    public XuanPaperItem(boolean empty) {
        super(new Item.Properties().stacksTo(empty ? 64 : 1).tab(SCACreativeTab.CALLIGRAPHY));
        this.empty = empty;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (!empty) {
            pTooltipComponents.add(new TranslatableComponent(HOVER_AUTHOR).append(getAuthor(pStack)));
        }
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        if (!empty) {
            consumer.accept(new IItemRenderProperties() {
                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return games.moegirl.sinocraft.sinocalligraphy.client.XuanPaperRenderer.getInstance();
                }
            });
        }
    }

    public static byte[] getDraw(ItemStack paper) {
        if (hasDraw(paper)) {
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
            return Objects.requireNonNullElseGet(component, () -> new TranslatableComponent(HOVER_AUTHOR_ERROR, name));
        }
        return new TranslatableComponent(HOVER_AUTHOR_EMPTY);
    }

    public static ItemStack createDraw(byte[] draw, Component author) {
        ItemStack paper = new ItemStack(SCAItems.XUAN_PAPER_FILLED.get());
        CompoundTag tag = paper.getOrCreateTag();
        tag.putByteArray(TAG_PIXELS, adjustSize(draw));
        tag.putString(TAG_AUTHOR, Component.Serializer.toJson(author));
        return paper;
    }

    public static ItemStack createDraw(byte[] draw) {
        ItemStack paper = new ItemStack(SCAItems.XUAN_PAPER_FILLED.get());
        CompoundTag tag = paper.getOrCreateTag();
        tag.putByteArray(TAG_PIXELS, adjustSize(draw));
        return paper;
    }

    public static boolean hasDraw(ItemStack paper) {
        return paper.is(SCAItems.XUAN_PAPER_FILLED.get()) && paper.hasTag() && paper.getOrCreateTag().contains(TAG_PIXELS, Tag.TAG_BYTE_ARRAY);
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
