package games.moegirl.sinocraft.sinocalligraphy.drawing;

import com.google.common.base.Verify;
import com.mojang.blaze3d.platform.NativeImage;
import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * <ul>
 *     <li>Use a CompoundTag save the draw instead of two Tag key, add version to nbt</li>
 *     <li>Use shorter string value instead of json data</li>
 * </ul>
 */
public class BrushV2Version extends DrawVersion {

    public static final String TAG_HOLDER = SinoCalligraphy.MODID + ".brush";
    public static final String TAG_VERSION = "version";
    public static final String TAG_PIXELS = "pixels";
    public static final String TAG_AUTHOR = "author";
    public static final String SYMBOL = "DBRUSH2";

    BrushV2Version() {
        super(null);
    }

    @Override
    protected boolean match(String value) {
        return value.startsWith(SYMBOL);
    }

    @Override
    protected boolean match(FriendlyByteBuf value) {
        return BrushV1Version.match(value, SYMBOL);
    }

    @Override
    protected boolean match(CompoundTag value) {
        return value.contains(TAG_HOLDER, Tag.TAG_COMPOUND) && SYMBOL.equals(value.getCompound(TAG_HOLDER).getString(TAG_VERSION));
    }

    @Override
    public DrawHolder read(String value) {
        DrawHolder holder = newDraw();
        value = value.substring(SYMBOL.length());
        byte[] draw = (byte[]) holder.data();
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
            BrushV1Version.read(value.getCompound(TAG_HOLDER), TAG_PIXELS, TAG_AUTHOR, holder);
        }
        return holder;
    }

    @Override
    public void write(DrawHolder holder, StringBuffer sb) {
        sb.append(SYMBOL);
        for (byte b : (byte[]) holder.data()) {
            Verify.verify(b >= 0, "Value must not negative");
            Verify.verify(b < 36, "Value must less than 36");
            sb.append(Integer.toString(b, 36));
        }
        if (holder.hasAuthor()) {
            sb.append(holder.authorAsString());
        }
    }

    @Override
    public void write(DrawHolder holder, FriendlyByteBuf buf) {
        buf.writeUtf(SYMBOL);
        buf.writeBoolean(holder.hasAuthor());
        buf.writeByteArray((byte[]) holder.data());
        if (holder.hasAuthor()) {
            buf.writeUtf(holder.authorAsString());
        }
    }

    @Override
    public void write(DrawHolder holder, CompoundTag tag) {
        CompoundTag t = new CompoundTag();
        t.putString(TAG_VERSION, SYMBOL);
        t.putByteArray(TAG_PIXELS, (byte[]) holder.data());
        if (holder.hasAuthor()) {
            t.putString(TAG_AUTHOR, holder.authorAsString());
        }
        tag.put(TAG_HOLDER, t);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public NativeImage toImage(DrawHolder holder) {
        return ((SmallBlackWhiteBrushHolder) holder).toImage();
    }

    @Override
    public DrawHolder newDraw() {
        return new SmallBlackWhiteBrushHolder(this);
    }
}
