package games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.version;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.holder.HolderByte32;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.FastColor;

import java.util.function.Supplier;

import static games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.holder.HolderByte32.SIZE;

/**
 * Origin draw version for brush
 */
public class VersionBrush1 extends DrawVersion {

    public static final String TAG_PIXELS = "pixels";
    public static final String TAG_AUTHOR = "author";
    public static final String SYMBOL = "DBRUSH1";

    VersionBrush1() {
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
        DrawHolder holder = newDraw();
        try {
            String json = value.substring(SYMBOL.length());
            JsonObject object = JsonParser.parseString(json).getAsJsonObject();
            byte[] draw = (byte[]) holder.getData();
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
        DrawHolder holder = newDraw();
        value.readUtf();
        boolean hasAuthor = value.readBoolean();
        holder.setDraw(value.readByteArray());
        holder.setAuthor(hasAuthor ? value.readUtf() : null);
        return holder;
    }

    @Override
    public DrawHolder read(CompoundTag value) {
        DrawHolder holder = newDraw();
        read(value, TAG_PIXELS, TAG_AUTHOR, holder);
        return holder;
    }

    @Override
    public void write(DrawHolder holder, StringBuffer sb) {
        sb.append(SYMBOL);
        JsonObject json = new JsonObject();
        JsonArray pixels = new JsonArray();
        for (byte b : (byte[]) holder.getData()) {
            pixels.add(b);
        }
        json.add(TAG_PIXELS, pixels);
        if (holder.hasAuthor()) {
            json.addProperty(TAG_AUTHOR, holder.getAuthorAsString());
        }
        sb.append(json);
    }

    @Override
    public void write(DrawHolder holder, FriendlyByteBuf buf) {
        buf.writeUtf(SYMBOL);
        buf.writeBoolean(holder.hasAuthor());
        buf.writeByteArray((byte[]) holder.getData());
        if (holder.hasAuthor()) {
            buf.writeUtf(holder.getAuthorAsString());
        }
    }

    @Override
    public void write(DrawHolder holder, CompoundTag tag) {
        tag.putByteArray(TAG_PIXELS, (byte[]) holder.getData());
        if (holder.hasAuthor()) {
            tag.putString(TAG_AUTHOR, holder.getAuthorAsString());
        }
    }

    @Override
    public Supplier<NativeImage> toImage(DrawHolder holder) {
        return toGrayImage(holder);
    }

    @Override
    public DrawHolder newDraw() {
        return new HolderByte32(this);
    }

    @Override
    protected DrawHolder updateFrom(DrawHolder oldHolder) {
        return oldHolder;
    }

    public static boolean match(FriendlyByteBuf value, String symbol) {
        int index = value.readerIndex();
        boolean match = false;
        try {
            if (symbol.equals(value.readUtf())) {
                match = true;
            }
        } catch (Exception ignored) {
        } finally {
            value.readerIndex(index);
        }
        return match;
    }

    public static void read(CompoundTag tag, String pixels, String author, DrawHolder holder) {
        holder.setDraw(tag.getByteArray(pixels));
        if (tag.contains(author, Tag.TAG_STRING)) {
            holder.setAuthor(tag.getString(author));
        }
    }

    public static Supplier<NativeImage> toGrayImage(DrawHolder holder) {
        return () -> {
            NativeImage image = new NativeImage(SIZE, SIZE, false);
            int index = 0;
            byte[] data = (byte[]) holder.getData();
            for (int w = 0; w < SIZE; w++) {
                for (int h = 0; h < SIZE; h++) {
                    int color = 16 * (16 - data[index++]) - 1;
                    image.setPixelRGBA(w, h, FastColor.ARGB32.color(255, color, color, color));
                }
            }
            return image;
        };
    }
}
