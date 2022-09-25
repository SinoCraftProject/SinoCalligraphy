package games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.holder;

import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.version.DrawVersion;

/**
 * 以 32x32 个 byte 存储的图片
 */
public class HolderByte32 extends HolderBytesBase implements DrawHolder {

    public static final int SIZE = 32;
    public static final int PIXEL_COUNT = SIZE * SIZE;

    public HolderByte32(DrawVersion version) {
        super(version, PIXEL_COUNT);
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
}
