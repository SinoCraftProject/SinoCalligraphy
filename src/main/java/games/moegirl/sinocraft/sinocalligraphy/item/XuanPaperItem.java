package games.moegirl.sinocraft.sinocalligraphy.item;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class XuanPaperItem extends Item {

    public static final int SIZE = 32;
    public static final int PIXEL_COUNT = SIZE * SIZE;
    public static final String TAG_NAME = "pixels";
    public static final ResourceLocation HAS_DRAW = new ResourceLocation(SinoCalligraphy.MODID, "has_draw");

    public XuanPaperItem() {
        super(new Item.Properties().tab(SCACreativeTab.CALLIGRAPHY));
    }

    public static byte[] getDraw(ItemStack paper) {
        if (paper.hasTag()) {
            CompoundTag tag = paper.getOrCreateTag();
            if (tag.contains(TAG_NAME, Tag.TAG_BYTE_ARRAY)) {
                byte[] draw = tag.getByteArray(TAG_NAME);
                byte[] b = draw;
                if (draw.length != PIXEL_COUNT) {
                    b = new byte[PIXEL_COUNT];
                    System.arraycopy(draw, 0, b, 0, Math.min(draw.length, PIXEL_COUNT));
                }
                return b;
            }
        }
        return new byte[PIXEL_COUNT];
    }

    public static void setDraw(ItemStack paper, byte[] draw) {
        byte[] b = draw;
        if (draw.length != PIXEL_COUNT) {
            b = new byte[PIXEL_COUNT];
            int len = Math.min(PIXEL_COUNT, draw.length);
            System.arraycopy(draw, 0, b, 0, len);
            if (len < PIXEL_COUNT) {
                Arrays.fill(b, len, PIXEL_COUNT, (byte) 0);
            }
        }
        paper.getOrCreateTag().putByteArray(TAG_NAME, b);
    }

    public static boolean hasDraw(ItemStack paper) {
        return paper.hasTag() && paper.getOrCreateTag().contains(TAG_NAME, Tag.TAG_BYTE_ARRAY);
    }

    public static void removeDraw(ItemStack paper) {
        if (paper.hasTag()) {
            paper.getOrCreateTag().remove(TAG_NAME);
        }
    }

    @Deprecated(forRemoval = true, since = "1.18.2-1.3.0")
    public static ItemStack convert(ItemStack stack) {
        ItemStack newStack = new ItemStack(SCAItems.XUAN_PAPER.get());
        newStack.setCount(stack.getCount());
        if (stack.is(SCAItems.XUAN_PAPER_FILLED.get())) {
            byte[] draw = stack.getOrCreateTag().getByteArray(XuanPaperItem.TAG_NAME);
            XuanPaperItem.setDraw(newStack, draw);
        }
        return newStack;
    }
}
