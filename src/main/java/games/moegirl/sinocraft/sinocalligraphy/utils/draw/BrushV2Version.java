package games.moegirl.sinocraft.sinocalligraphy.utils.draw;

import com.google.common.base.Verify;
import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.awt.image.BufferedImage;

public class BrushV2Version extends DrawVersion {

    public static final String TAG_HOLDER = SinoCalligraphy.MODID + ".brush";
    public static final String TAG_VERSION = "pixels";
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
        BrushV2Holder holder = new BrushV2Holder();
        value = value.substring(SYMBOL.length());
        byte[] draw = holder.getDraw();
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
        BrushV2Holder holder = new BrushV2Holder();
        value.readUtf();
        boolean hasAuthor = value.readBoolean();
        holder.setDraw(value.readByteArray());
        holder.setAuthor(hasAuthor ? value.readUtf() : null);
        return holder;
    }

    @Override
    public DrawHolder read(CompoundTag value) {
        BrushV2Holder holder = new BrushV2Holder();
        if (value.contains(TAG_HOLDER, Tag.TAG_COMPOUND)) {
            BrushV1Version.read(value.getCompound(TAG_HOLDER), TAG_PIXELS, TAG_AUTHOR, holder);
        }
        return holder;
    }

    @Override
    public void write(DrawHolder draw, StringBuffer sb) {
        sb.append(SYMBOL);
        for (byte b : (byte[]) draw.draw()) {
            Verify.verify(b < 36, "Value must less than 36");
            sb.append(Integer.toString(b, 36));
        }
        if (draw.hasAuthor()) {
            sb.append(draw.authorAsString());
        }
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
        CompoundTag t = new CompoundTag();
        t.putString(TAG_VERSION, SYMBOL);
        t.putByteArray(TAG_PIXELS, (byte[]) draw.draw());
        if (draw.hasAuthor()) {
            t.putString(TAG_AUTHOR, draw.authorAsString());
        }
        tag.put(TAG_HOLDER, t);
    }

    @Override
    public BufferedImage toImage(DrawHolder draw) {
        return ((SmallBlackWhiteBrushHolder) draw).toImage();
    }

    @Override
    protected DrawHolder updateFromPrev(DrawHolder draw) {
        BrushV2Holder holder = new BrushV2Holder();
        DrawHolder.copyDirectly(draw, holder);
        return holder;
    }
}
