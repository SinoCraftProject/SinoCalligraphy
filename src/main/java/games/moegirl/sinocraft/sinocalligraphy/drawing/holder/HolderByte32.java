package games.moegirl.sinocraft.sinocalligraphy.drawing.holder;

import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.version.DrawVersion;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * A draw holder with a 32*32 black-white draw
 */
public class HolderByte32 implements DrawHolder {

    public static final int SIZE = 32;
    public static final int PIXEL_COUNT = SIZE * SIZE;

    private final DrawVersion version;

    private byte[] value;
    @Nullable
    private Component author;

    public HolderByte32(DrawVersion version) {
        this.version = version;

        setDraw(new byte[PIXEL_COUNT]);
        setAuthor((Component) null);
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
    public boolean isEmpty() {
        for (byte b : value) {
            if (b != 0) {
                return false;
            }
        }
        return true;
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
}
