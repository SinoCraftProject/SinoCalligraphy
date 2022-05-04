package games.moegirl.sinocraft.sinocalligraphy.utils.draw;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class DrawVersion {

    private static final List<DrawVersion> VERSIONS = new LinkedList<>();

    public static void register(DrawVersion version) {
        VERSIONS.add(version);
    }

    public static Optional<DrawVersion> find(String value) {
        for (int i = VERSIONS.size() - 1; i >= 0; i--) {
            DrawVersion version = VERSIONS.get(i);
            if (version.match(value)) {
                return Optional.of(version);
            }
        }
        return Optional.empty();
    }

    public static Optional<DrawVersion> find(FriendlyByteBuf value) {
        for (int i = VERSIONS.size() - 1; i >= 0; i--) {
            DrawVersion version = VERSIONS.get(i);
            if (version.match(value)) {
                return Optional.of(version);
            }
        }
        return Optional.empty();
    }

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
        while (version.nextDrawer != null) {
            holder = version.nextDrawer.updateFromPrev(holder);
            version = holder.version();
        }
        return holder;
    }

    @Nullable
    private DrawVersion nextDrawer = null;

    public DrawVersion(@Nullable DrawVersion prevVersion) {
        if (prevVersion != null) {
            if (prevVersion.nextDrawer != null) {
                throw new IllegalArgumentException("Previous version " + prevVersion + " has contained a next version " + prevVersion.nextDrawer);
            }
            prevVersion.nextDrawer = this;
        }
    }

    protected abstract boolean match(String value);

    protected abstract boolean match(FriendlyByteBuf value);

    protected abstract boolean match(CompoundTag value);

    public abstract DrawHolder read(String value);

    public abstract DrawHolder read(FriendlyByteBuf value);

    public abstract DrawHolder read(CompoundTag value);

    public abstract void write(DrawHolder draw, StringBuffer sb);

    public abstract void write(DrawHolder draw, FriendlyByteBuf buf);

    public abstract void write(DrawHolder draw, CompoundTag tag);

    public abstract BufferedImage toImage(DrawHolder draw);

    protected abstract DrawHolder updateFromPrev(DrawHolder draw);
}
