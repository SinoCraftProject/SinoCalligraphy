package games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.version;

import com.mojang.blaze3d.platform.NativeImage;
import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.Constants;
import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.holder.HolderByte32;
import games.moegirl.sinocraft.sinocalligraphy.utility.DrawHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.FastColor;

import java.util.function.Supplier;

import static games.moegirl.sinocraft.sinocalligraphy.drawing.legacy.holder.HolderByte32.SIZE;

/**
 * <ul>
 *     <li>Use a CompoundTag save the draw instead of two Tag key, add version to nbt</li>
 *     <li>Use shorter string value instead of json data</li>
 * </ul>
 */
// todo todo todo todo
public class VersionBrush3White extends DrawVersion {

    public static final String VERSION = "BrushV3W";
    public static final int foreground = FastColor.ARGB32.color(0, 0, 0, 0);
    public static final int background = FastColor.ARGB32.color(255, 255, 255, 255);

    VersionBrush3White() {
        super(DrawVersions.BRUSH_V2);
    }

    @Override
    protected boolean match(String str) {
        return str.equals(VERSION);
    }

    @Override
    protected boolean match(FriendlyByteBuf buf) {
        var ver = buf.readUtf();

        System.out.println("ver: " + ver);
        System.out.println("VER: " + VERSION);
        if (ver.equals(VERSION)) {
            buf.readNbt();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean match(CompoundTag value) {
        return value.contains(Constants.TAG_HOLDER, Tag.TAG_COMPOUND)
                && VERSION.equals(value.getCompound(Constants.TAG_HOLDER).getString(Constants.TAG_VERSION));
    }

    @Override
    public DrawHolder read(String value) {
        var holder = newDraw();
        return holder;
    }

    @Override
    public DrawHolder read(FriendlyByteBuf buf) {
        var ver = buf.readUtf();

        if (!ver.equals(VERSION)) {
            System.out.println("VER3:" + ver);
            return newDraw();
        }

        var nbt = buf.readNbt();
        if (nbt == null
                || !nbt.contains(Constants.TAG_VERSION)
                || !nbt.getString(Constants.TAG_VERSION).equals(VERSION)) {
            System.out.println("VER3:" + nbt.getString(Constants.TAG_VERSION));
            return newDraw();
        }

        var holder = newDraw();
        // todo holder.deserializeNBT(nbt);
        return holder;
    }

    @Override
    public DrawHolder read(CompoundTag tag) {
        var holder = newDraw();
        // todo holder.deserializeNBT(tag);
        return holder;
    }

    @Override
    public void write(DrawHolder holder, StringBuffer sb) {
    }

    @Override
    public void write(DrawHolder holder, FriendlyByteBuf buf) {
    }

    @Override
    public void write(DrawHolder holder, CompoundTag tag) {
    }

    @Override
    public Supplier<NativeImage> toImage(DrawHolder holder) {
        return () -> {
            NativeImage image = new NativeImage(SIZE, SIZE, false);
            var bgColor = DrawHelper.toNativeImage(background);
            image.fillRect(0, 0, SIZE, SIZE, bgColor);
            int index = 0;
            byte[] value = (byte[]) holder.getData();
            for (int w = 0; w < SIZE; w++) {
                for (int h = 0; h < SIZE; h++) {
                    var alpha = 16 * (16 - value[index++]) - 1;

                    if (alpha == 255) {
                        continue;
                    }

                    int mixed = DrawHelper.mix(foreground, background, alpha);
                    var abgr = DrawHelper.toNativeImage(mixed);
                    image.setPixelRGBA(w, h, abgr);
                }
            }
            return image;
        };
    }

    @Override
    public DrawHolder newDraw() {
        return new HolderByte32(this);
    }

    @Override
    protected DrawHolder updateFrom(DrawHolder oldHolder) {
        var holder = DrawVersions.BRUSH_V3_WHITE.newDraw();

        //todo update

        return holder;
    }
}
