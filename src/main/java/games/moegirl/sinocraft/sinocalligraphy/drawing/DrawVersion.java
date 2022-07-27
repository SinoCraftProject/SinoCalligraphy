package games.moegirl.sinocraft.sinocalligraphy.drawing;

import com.google.gson.Gson;
import com.mojang.blaze3d.platform.NativeImage;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class DrawVersion {
    public static final Gson GSON = new Gson();

    private static final List<DrawVersion> VERSIONS = new LinkedList<>();

    /**
     * Register a version, use for finding version from other data
     * @param version version
     */
    public static void register(DrawVersion version) {
        VERSIONS.add(version);
    }

    /**
     * Find version from string
     * @param versionId string from {@link #write(DrawHolder, StringBuffer)}
     * @return version if exist
     */
    public static Optional<DrawVersion> find(String versionId) {
        for (int i = VERSIONS.size() - 1; i >= 0; i--) {
            DrawVersion holder = VERSIONS.get(i);
            if (holder.match(versionId)) {
                return Optional.of(holder);
            }
        }

        return Optional.empty();
    }

    /**
     * Find version from network
     * @param buf buffer from {@link #write(DrawHolder, FriendlyByteBuf)}
     * @return version if exist
     */
    public static Optional<DrawVersion> find(FriendlyByteBuf buf) {
        var ver = buf.readUtf();
        return find(ver);
    }

    /**
     * Find version from nbt
     * @param tag tag from {@link #write(DrawHolder, CompoundTag)}
     * @return version if exist
     */
    public static Optional<DrawVersion> find(@Nullable CompoundTag tag) {
        if (tag == null) {
            return Optional.empty();
        }

        if (!tag.contains(Constants.TAG_VERSION)) {
            return Optional.empty();
        }

        var versionId = tag.getString(Constants.TAG_VERSION);

        return find(versionId);
    }

    public static DrawHolder update(DrawHolder draw) {
        DrawHolder holder = draw;
        DrawVersion version = draw.getVersion();
        while (version.nextVersion != null) {
            holder = version.nextVersion.updateFrom(holder);
            version = holder.getVersion();
        }
        return holder;
    }

    @Nullable
    private DrawVersion nextVersion = null;

    @SuppressWarnings("CopyConstructorMissesField")
    public DrawVersion(@Nullable DrawVersion prevVersion) {
        if (prevVersion != null) {
            if (prevVersion.nextVersion != null) {
                throw new IllegalArgumentException("Previous version " + prevVersion + " has contained a new version " + prevVersion.nextVersion);
            }
            prevVersion.nextVersion = this;
        }
    }

    /**
     * Check if the value is suitable for this version
     * @param value string value
     * @return true if the version is suitable for this version
     */
    protected abstract boolean match(String value);

    /**
     * Check if the value is suitable for this version
     * @param value network buffer
     * @return true if the version is suitable for this version
     */
    protected abstract boolean match(FriendlyByteBuf value);

    /**
     * Check if the value is suitable for this version
     * @param value nbt tag
     * @return true if the version is suitable for this version
     */
    protected abstract boolean match(CompoundTag value);

    /**
     * Read a DrawHolder from the value
     * @param value string value
     * @return holder
     */
    public abstract DrawHolder read(String value);

    /**
     * Read a DrawHolder from the value
     * @param value network buffer
     * @return holder
     */
    public abstract DrawHolder read(FriendlyByteBuf value);

    /**
     * Read a DrawHolder from the value
     * @param value nbt tag
     * @return holder
     */
    public abstract DrawHolder read(CompoundTag value);

    /**
     * Write draw to a string
     * @param holder draw holder suitable for the version
     * @param sb string buffer
     */
    public abstract void write(DrawHolder holder, StringBuffer sb);

    /**
     * Write draw to a network
     * @param holder draw holder suitable for the version
     * @param buf network buffer
     */
    public abstract void write(DrawHolder holder, FriendlyByteBuf buf);

    /**
     * Write draw to nbt
     * @param holder draw holder suitable for the version
     * @param tag nbt tag
     */
    public abstract void write(DrawHolder holder, CompoundTag tag);

    /**
     * Create a BufferedImage from a draw
     * @param holder holder suitable the version
     * @return image
     */
    public abstract NativeImage toImage(DrawHolder holder);

    /**
     * Create a new draw holder
     * @return holder
     */
    public abstract DrawHolder newDraw();

    /**
     * Create a new draw holder with type.
     * Ignore type by default for BrushV1 and BrushV2.
     *
     * @param type Type of paper.
     * @return holder
     * @since BrushV3
     */
    public abstract DrawHolder newDraw(XuanPaperType type);

    /**
     * Create a holder from the old version
     * @param oldHolder old version holder
     * @return new version holder
     */
    protected DrawHolder updateFrom(DrawHolder oldHolder) {
        DrawHolder holder = newDraw();
        holder.apply(oldHolder);
        return holder;
    }

    protected String getVersionId() {
        return "oldVersion";
    }

    public static Optional<DrawVersion> from(String str) {
        CompoundTag nbt = null;

        try {
            GSON.fromJson(str, Object.class);
            nbt = TagParser.parseTag(str);
        } catch (Exception ignored) {

        }

        if (nbt != null) {
            return find(nbt);
        } else {
            return find(str);
        }
    }
}
