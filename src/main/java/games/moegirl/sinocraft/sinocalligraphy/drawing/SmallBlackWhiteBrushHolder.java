package games.moegirl.sinocraft.sinocalligraphy.drawing;

import com.mojang.blaze3d.platform.NativeImage;
import games.moegirl.sinocraft.sinocalligraphy.utility.DrawHelper;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * A draw holder with a 32*32 black-white draw, saved by byte array
 */
public class SmallBlackWhiteBrushHolder implements DrawHolder {

    public static final int SIZE = 32;
    public static final int PIXEL_COUNT = SIZE * SIZE;

    private byte[] value;
    @Nullable
    private Component author;
    private DrawVersion version;

    private XuanPaperType type = null;

    public SmallBlackWhiteBrushHolder(DrawVersion version) {
        this.version = version;

        setDraw(new byte[PIXEL_COUNT]);
        setAuthor((Component) null);

        type = XuanPaperType.WHITE;
    }

    public SmallBlackWhiteBrushHolder(DrawVersion version, XuanPaperType typeIn) {
        this.version = version;

        setDraw(new byte[PIXEL_COUNT]);
        setAuthor((Component) null);

        type = typeIn;
    }

    @Override
    public byte[] getData() {
        return value;
    }

    @Override
    public void setDraw(byte[] data) {
        this.value = adjustSize(data);
    }

    @Override
    public Component getAuthor() {
        return author == null ? DEFAULT_AUTHOR : author;
    }

    @Override
    public void setAuthor(@Nullable Component author) {
        this.author = author;
    }

    @Override
    public boolean hasAuthor() {
        return author != null;
    }

    @Override
    public DrawVersion getVersion() {
        return version;
    }

    @Override
    public void setVersion(DrawVersion versionIn) {
        version = versionIn;
    }

    @Override
    public boolean isEmpty() {
        for (byte b : value) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public XuanPaperType getType() {
        return type;
    }

    @Override
    public void setType(XuanPaperType typeIn) {
        type = typeIn;
    }

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();

        tag.putString(Constants.TAG_VERSION, getVersion().getVersionId());
        tag.putString(Constants.TAG_AUTHOR, getAuthorAsString());
        tag.putByteArray(Constants.TAG_PIXELS, getData());
        tag.putString(Constants.TAG_TYPE, getType().getName());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        setVersion(DrawVersions.LATEST_BRUSH_VERSION);
        setAuthor(DEFAULT_AUTHOR);
        setDraw(new byte[0]);

        if (tag.contains(Constants.TAG_VERSION)) {
            var version = tag.getString(Constants.TAG_VERSION);
            if (!version.equals(getVersion().getVersionId())) {
                // Todo: qyl27: update from old versions.
                return;
            }
        }

        if (tag.contains(Constants.TAG_AUTHOR)) {
            setAuthor(tag.getString(Constants.TAG_AUTHOR));
        }

        if (tag.contains(Constants.TAG_PIXELS)) {
            setDraw(tag.getByteArray(Constants.TAG_PIXELS));
        }

        if (tag.contains(Constants.TAG_TYPE)) {
            var type = XuanPaperType.of(tag.getString(Constants.TAG_TYPE));
            setType(type);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public DrawRender render() {
        return new SmallBlockWhiteBrushRender(this);
    }

    public byte[] getDraw() {
        return value;
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

    public NativeImage toImage() {
        NativeImage image = new NativeImage(SIZE, SIZE, false);
        image.fillRect(0, 0, SIZE, SIZE, type.getBackground());

        int index = 0;
        for (int w = 0; w < SmallBlackWhiteBrushHolder.SIZE; w++) {
            for (int h = 0; h < SmallBlackWhiteBrushHolder.SIZE; h++) {
                var alpha = 16 * (16 - value[index++]) - 1;

                if (alpha == 255) {
                    continue;
                }

                var foreground = DrawHelper.invert(type.getForeground());
                var argb = FastColor.ARGB32.color(alpha,
                        FastColor.ARGB32.red(foreground),
                        FastColor.ARGB32.green(foreground),
                        FastColor.ARGB32.blue(foreground));

                image.setPixelRGBA(w, h, argb);
            }
        }
        return image;
    }
}
