package games.moegirl.sinocraft.sinocalligraphy.utils.draw;

import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    private final DrawVersion version;

    public SmallBlackWhiteBrushHolder(DrawVersion version) {
        this.version = version;

        setDraw(new byte[PIXEL_COUNT]);
        setAuthor((Component) null);
    }

    @Override
    public Object data() {
        return value;
    }

    @Override
    public void setDraw(Object data) {
        this.value = adjustSize((byte[]) data);
    }

    @Override
    public Component author() {
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
    public DrawVersion version() {
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

    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        int index = 0;
        for (int w = 0; w < SmallBlackWhiteBrushHolder.SIZE; w++) {
            for (int h = 0; h < SmallBlackWhiteBrushHolder.SIZE; h++) {
                float color = 0.0625f * (16 - value[index++]);
                image.setRGB(w, h, new Color(color, color, color).getRGB());
            }
        }
        return image;
    }
}
