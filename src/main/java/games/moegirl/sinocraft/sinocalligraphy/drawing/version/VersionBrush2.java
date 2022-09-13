package games.moegirl.sinocraft.sinocalligraphy.drawing.version;

import com.google.common.base.Verify;
import com.mojang.blaze3d.platform.NativeImage;
import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.holder.HolderByte32;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

/**
 * <ul>
 *     <li>Use a CompoundTag save the draw instead of two Tag key, add version to nbt</li>
 *     <li>Use shorter string value instead of json data</li>
 * </ul>
 */
public class VersionBrush2 extends DrawVersion {

    public static final String TAG_HOLDER = SinoCalligraphy.MODID + ".brush";
    public static final String TAG_VERSION = "version";
    public static final String TAG_PIXELS = "pixels";
    public static final String TAG_AUTHOR = "author";
    public static final String SYMBOL = "DBRUSH2";

    VersionBrush2() {
        super(null);
    }

    @Override
    protected boolean match(String value) {
        return value.startsWith(SYMBOL);
    }

    @Override
    protected boolean match(FriendlyByteBuf value) {
        return VersionBrush1.match(value, SYMBOL);
    }

    @Override
    protected boolean match(CompoundTag value) {
        return value.contains(TAG_HOLDER, Tag.TAG_COMPOUND) && SYMBOL.equals(value.getCompound(TAG_HOLDER).getString(TAG_VERSION));
    }

    @Override
    public DrawHolder read(String value) {
        DrawHolder holder = newDraw();
        value = value.substring(SYMBOL.length());
        byte[] draw = holder.getData();
        for (int i = 0; i < draw.length; i++) {
            draw[i] = Byte.parseByte(Character.toString(value.charAt(i)), 36);
        }
        if (value.length() > draw.length) {
            holder.setAuthor(value.substring(draw.length));
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
        if (value.contains(TAG_HOLDER, Tag.TAG_COMPOUND)) {
            VersionBrush1.read(value.getCompound(TAG_HOLDER), TAG_PIXELS, TAG_AUTHOR, holder);
        }
        return holder;
    }

    @Override
    public void write(DrawHolder holder, StringBuffer sb) {
        sb.append(SYMBOL);
        for (byte b : holder.getData()) {
            Verify.verify(b >= 0, "Value must not negative");
            Verify.verify(b < 36, "Value must less than 36");
            sb.append(Integer.toString(b, 36));
        }
        if (holder.hasAuthor()) {
            sb.append(holder.getAuthorAsString());
        }
    }

    @Override
    public void write(DrawHolder holder, FriendlyByteBuf buf) {
        buf.writeUtf(SYMBOL);
        buf.writeBoolean(holder.hasAuthor());
        buf.writeByteArray(holder.getData());
        if (holder.hasAuthor()) {
            buf.writeUtf(holder.getAuthorAsString());
        }
    }

    @Override
    public void write(DrawHolder holder, CompoundTag tag) {
        CompoundTag t = new CompoundTag();
        t.putString(TAG_VERSION, SYMBOL);
        t.putByteArray(TAG_PIXELS, holder.getData());
        if (holder.hasAuthor()) {
            t.putString(TAG_AUTHOR, holder.getAuthorAsString());
        }
        tag.put(TAG_HOLDER, t);
    }

    @Override
    public Supplier<NativeImage> toImage(DrawHolder holder) {
        return VersionBrush1.toGrayImage(holder);
    }

    @Override
    public DrawHolder newDraw() {
        return new HolderByte32(this);
    }

    @Override
    public DrawHolder newDraw(XuanPaperType type) {
        return newDraw();
    }
}
