package games.moegirl.sinocraft.sinocalligraphy.drawing;

import com.mojang.blaze3d.platform.NativeImage;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * <ul>
 *     <li>Use a CompoundTag save the draw instead of two Tag key, add version to nbt</li>
 *     <li>Use shorter string value instead of json data</li>
 * </ul>
 */
public class BrushV3Version extends DrawVersion {
    public static final String VERSION = "BrushV3";

    BrushV3Version() {
        super(DrawVersions.BRUSH_V2);
    }

    @Override
    protected boolean match(String str) {
        return str.equals(VERSION);
    }

//    private String getVersion(String str) {
//        try {
//            GSON.fromJson(str, Object.class);
//
//            var nbt = TagParser.parseTag(str);
//            if (nbt.contains(Constants.TAG_VERSION)) {
//                return nbt.getString(Constants.TAG_VERSION);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return "";
//    }

    @Override
    protected String getVersionId() {
        return VERSION;
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
        try {
            var tag = TagParser.parseTag(value);
            holder.deserializeNBT(tag);
        } catch (Exception ignored) {
//            ex.printStackTrace();
        }

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
            return newDraw(XuanPaperType.WHITE);
        }

        var holder = newDraw(XuanPaperType.WHITE);
        holder.deserializeNBT(nbt);
        return holder;

//        var version = buf.readUtf();
//
//        System.out.println("ver2: " + version);
//        System.out.println("VER: " + VERSION);
//
//        if (!version.equals(VERSION)) {
//            System.out.println(version);
//            return newDraw();
//        }
//
//        var draw = buf.readByteArray();
//        var author = buf.readUtf();
//        var typeStr = buf.readUtf();
//
//        var type = XuanPaperType.of(typeStr);
//
//        DrawHolder holder = newDraw(type);
//        holder.setDraw(draw);
//        holder.setAuthor(author);
//        return holder;
    }

    @Override
    public DrawHolder read(CompoundTag tag) {
        var holder = newDraw();
        holder.deserializeNBT(tag);
        return holder;
    }

    @Override
    public void write(DrawHolder holder, StringBuffer sb) {
        var nbt = holder.serializeNBT();
        var str = nbt.toString();
        sb.append(str);
    }

    @Override
    public void write(DrawHolder holder, FriendlyByteBuf buf) {
        buf.writeUtf(VERSION);
        buf.writeNbt(holder.serializeNBT());
//
//        System.out.println(VERSION);
//        System.out.println(holder.getVersion().getVersionId());
//
//        buf.writeUtf(VERSION);
//        buf.writeByteArray(holder.getData());
//        buf.writeUtf(holder.getAuthorAsString());
//        buf.writeUtf(holder.getType().getName());
    }

    @Override
    public void write(DrawHolder holder, CompoundTag tag) {
        tag.put(Constants.TAG_HOLDER, holder.serializeNBT());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public NativeImage toImage(DrawHolder holder) {
        return ((SmallBlackWhiteBrushHolder) holder).toImage();
    }

    @Override
    public DrawHolder newDraw() {
        return new SmallBlackWhiteBrushHolder(this, XuanPaperType.WHITE);
    }

    @Override
    public DrawHolder newDraw(XuanPaperType type) {
        return new SmallBlackWhiteBrushHolder(this, type);
    }

    @Override
    protected DrawHolder updateFrom(DrawHolder oldHolder) {
        var holder = DrawVersions.BRUSH_V3.newDraw();

        if (oldHolder.getVersion() == DrawVersions.BRUSH_V1) {
            holder.setDraw(oldHolder.getData());
            holder.setAuthor(DrawHolder.DEFAULT_AUTHOR);
            holder.setType(XuanPaperType.WHITE);
        }

        if (oldHolder.getVersion() == DrawVersions.BRUSH_V2) {
            holder.setDraw(oldHolder.getData());
            holder.setAuthor(oldHolder.getAuthor());
            holder.setType(XuanPaperType.WHITE);
        }

        return holder;
    }
}
