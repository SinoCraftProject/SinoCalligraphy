package games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.holder;

import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.version.DrawVersion;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * 以 byte[] 存储图片的容器
 */
public abstract class HolderBytesBase implements DrawHolder {

    private final DrawVersion version;
    private final int pixelCount;

    protected byte[] value;
    @Nullable
    private Component author;

    public HolderBytesBase(DrawVersion version, int pixelCount) {
        this.version = version;
        this.pixelCount = pixelCount;

        setDraw(new byte[pixelCount]);
        setAuthor((Component) null);
    }

    @Override
    public Object getData() {
        return value;
    }

    @Override
    public void setDraw(Object data) {
        this.value = adjustSize((byte[]) data, pixelCount);
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

    public byte[] getValue() {
        return value;
    }

    private static byte[] adjustSize(byte[] draw, int pixelCount) {
        byte[] b = draw;
        if (draw.length != pixelCount) {
            b = new byte[pixelCount];
            int len = Math.min(pixelCount, draw.length);
            System.arraycopy(draw, 0, b, 0, len);
            if (len < pixelCount) {
                Arrays.fill(b, len, pixelCount, (byte) 0);
            }
        }
        return b;
    }
}
