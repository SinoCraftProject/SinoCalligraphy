package games.moegirl.sinocraft.sinocalligraphy.utils.draw;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.awt.image.BufferedImage;

public class BrushV1Version extends DrawVersion {

    public static final String TAG_PIXELS = "pixels";
    public static final String TAG_AUTHOR = "author";
    public static final String SYMBOL = "DBRUSH1";

    BrushV1Version() {
        super(null);
    }

    @Override
    protected boolean match(String value) {
        return value.startsWith(SYMBOL);
    }

    @Override
    protected boolean match(FriendlyByteBuf value) {
        return match(value, SYMBOL);
    }

    @Override
    protected boolean match(CompoundTag value) {
        return value.contains(TAG_PIXELS, Tag.TAG_BYTE_ARRAY);
    }

    @Override
    public DrawHolder read(String value) {
        BrushV1Holder holder = new BrushV1Holder();
        try {
            String json = value.substring(SYMBOL.length());
            JsonObject object = JsonParser.parseString(json).getAsJsonObject();
            byte[] draw = holder.getDraw();
            if (object.has(TAG_PIXELS)) {
                JsonArray array = object.getAsJsonArray(TAG_PIXELS);
                int len = Math.min(draw.length, array.size());
                for (int i = 0; i < len; i++) {
                    draw[i] = array.get(i).getAsByte();
                }
            }
            if (object.has(TAG_AUTHOR)) {
                holder.setAuthor(object.get(TAG_AUTHOR).getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holder;
    }

    @Override
    public DrawHolder read(FriendlyByteBuf value) {
        BrushV1Holder holder = new BrushV1Holder();
        value.readUtf();
        boolean hasAuthor = value.readBoolean();
        holder.setDraw(value.readByteArray());
        holder.setAuthor(hasAuthor ? value.readUtf() : null);
        return holder;
    }

    @Override
    public DrawHolder read(CompoundTag value) {
        BrushV1Holder holder = new BrushV1Holder();
        read(value, TAG_PIXELS, TAG_AUTHOR, holder);
        return holder;
    }

    @Override
    public void write(DrawHolder draw, StringBuffer sb) {
        sb.append(SYMBOL);
        JsonObject json = new JsonObject();
        JsonArray pixels = new JsonArray();
        for (byte b : (byte[]) draw.draw()) {
            pixels.add(b);
        }
        json.add(TAG_PIXELS, pixels);
        if (draw.hasAuthor()) {
            json.addProperty(TAG_AUTHOR, draw.authorAsString());
        }
        sb.append(json);
    }

    @Override
    public void write(DrawHolder draw, FriendlyByteBuf buf) {
        buf.writeUtf(SYMBOL);
        buf.writeBoolean(draw.hasAuthor());
        buf.writeByteArray((byte[]) draw.draw());
        if (draw.hasAuthor()) {
            buf.writeUtf(draw.authorAsString());
        }
    }

    @Override
    public void write(DrawHolder draw, CompoundTag tag) {
        tag.putByteArray(TAG_PIXELS, (byte[]) draw.draw());
        if (draw.hasAuthor()) {
            tag.putString(TAG_AUTHOR, draw.authorAsString());
        }
    }

    @Override
    public BufferedImage toImage(DrawHolder draw) {
        return ((SmallBlackWhiteBrushHolder) draw).toImage();
    }

    @Override
    protected DrawHolder updateFromPrev(DrawHolder draw) {
        return draw;
    }

    public static boolean match(FriendlyByteBuf value, String symbol) {
        int index = value.readerIndex();
        try {
            if (symbol.equals(value.readUtf())) {
                value.readerIndex(index);
                return true;
            }
        } catch (Exception e) {
            value.readerIndex(index);
            return false;
        }
        return false;
    }

    public static void read(CompoundTag tag, String pixels, String author, DrawHolder holder) {
        holder.setDraw(tag.getByteArray(pixels));
        if (tag.contains(author, Tag.TAG_STRING)) {
            holder.setAuthor(tag.getString(author));
        }
    }
}
