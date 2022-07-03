package games.moegirl.sinocraft.sinocalligraphy.drawing;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class DrawVersion {

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
     * @param value string from {@link #write(DrawHolder, StringBuffer)}
     * @return version if exist
     */
    public static Optional<DrawVersion> find(String value) {
        for (int i = VERSIONS.size() - 1; i >= 0; i--) {
            DrawVersion version = VERSIONS.get(i);
            if (version.match(value)) {
                return Optional.of(version);
            }
        }
        return Optional.empty();
    }

    /**
     * Find version from network
     * @param value buffer from {@link #write(DrawHolder, FriendlyByteBuf)}
     * @return version if exist
     */
    public static Optional<DrawVersion> find(FriendlyByteBuf value) {
        for (int i = VERSIONS.size() - 1; i >= 0; i--) {
            DrawVersion version = VERSIONS.get(i);
            if (version.match(value)) {
                return Optional.of(version);
            }
        }
        return Optional.empty();
    }

    /**
     * Find version from nbt
     * @param value tag from {@link #write(DrawHolder, CompoundTag)}
     * @return version if exist
     */
    public static Optional<DrawVersion> find(@Nullable CompoundTag value) {
        if (value == null) {
            return Optional.empty();
        }
        for (int i = VERSIONS.size() - 1; i >= 0; i--) {
            DrawVersion version = VERSIONS.get(i);
            if (version.match(value)) {
                return Optional.of(version);
            }
        }
        return Optional.empty();
    }

    public static DrawHolder update(DrawHolder draw) {
        DrawHolder holder = draw;
        DrawVersion version = draw.version();
        while (version.nextVersion != null) {
            holder = version.nextVersion.updateFrom(holder);
            version = holder.version();
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
     * Create a holder from the old version
     * @param oldHolder old version holder
     * @return new version holder
     */
    protected DrawHolder updateFrom(DrawHolder oldHolder) {
        DrawHolder holder = newDraw();
        holder.apply(oldHolder);
        return holder;
    }
}
