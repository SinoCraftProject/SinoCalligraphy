package games.moegirl.sinocraft.sinocalligraphy.drawing.version;

import com.google.gson.Gson;
import com.mojang.blaze3d.platform.NativeImage;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 图像版本信息，包括
 * <ul>
 *     <li>可直接用于判断</li>
 *     <li>该版本对应的上一个/下一个版本</li>
 *     <li>从旧版本更新到当前版本</li>
 *     <li>序列化/反序列化（字符串，NBT数据，Buffer）</li>
 *     <li>创建用于存放图片的 {@link DrawHolder}</li>
 * </ul>
 */
public abstract class DrawVersion {
    public static final Gson GSON = new Gson();

    /**
     * 根据字符串查找符合的版本
     * @param version 当前图片适用场景的最新版本
     */
    public static Optional<DrawVersion> find(String draw, DrawVersion version) {
        while (version != null) {
            if (version.match(draw)) {
                return Optional.of(version);
            } else {
                version = version.prevVersion;
            }
        }
        return Optional.empty();
    }

    /**
     * 根据 buffer 查找符合的版本
     * <p>
     * <b>注意：保证 FriendlyByteBuf 的 position 不变</b>
     * @param version 当前图片适用场景的最新版本
     */
    public static Optional<DrawVersion> find(FriendlyByteBuf buf, DrawVersion version) {
        while (version != null) {
            if (version.match(buf)) {
                return Optional.of(version);
            } else {
                version = version.prevVersion;
            }
        }
        return Optional.empty();
    }

    /**
     * 根据 nbt 查找符合的版本
     * <p>
     * <b>注意：保证 NBT 不变</b>
     * @param version 当前图片适用场景的最新版本
     */
    public static Optional<DrawVersion> find(CompoundTag tag, DrawVersion version) {
        while (version != null) {
            if (version.match(tag)) {
                return Optional.of(version);
            } else {
                version = version.prevVersion;
            }
        }
        return Optional.empty();
    }

    /**
     * 升级：将给定图片升级到可用的最新存储版本
     * @param draw 给定图片
     */
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
    @Nullable
    private final DrawVersion prevVersion;

    /**
     * @param prevVersion 前一个版本。注意版本链应当是单向的。
     */
    public DrawVersion(@Nullable DrawVersion prevVersion) {
        if (prevVersion != null) {
            if (prevVersion.nextVersion != null) {
                throw new IllegalArgumentException("Previous version " + prevVersion + " has contained a new version " + prevVersion.nextVersion);
            }
            prevVersion.nextVersion = this;
        }
        this.prevVersion = prevVersion;
    }

    /**
     * 检查给定字符串是否由该版本生成
     */
    protected abstract boolean match(String value);

    /**
     * 检查给定 buffer 是否由该版本生成
     * <p>
     * <b>注意：给定 buffer 的 position 不应发生变化</b>
     */
    protected abstract boolean match(FriendlyByteBuf value);

    /**
     * 检查给定 nbt 是否由该版本生成
     * <p>
     * <b>注意：给定 nbt 不应发生变化</b>
     */
    protected abstract boolean match(CompoundTag value);

    /**
     * 从给定字符串反序列化生成图片
     * <p>
     * <b>注意：假定字符串是由该版本生成，不再进行检查</b>
     */
    public abstract DrawHolder read(String value);

    /**
     * 从给定 buffer 生成图片
     * <p>
     * <b>注意：假定 buffer 是由该版本生成，不再进行检查</b>
     * <b>注意：buffer 的 position 应当指向读完图片之后的位置</b>
     */
    public abstract DrawHolder read(FriendlyByteBuf value);

    /**
     * 从给定 nbt 生成图片
     * <p>
     * <b>注意：假定 nbt 是由该版本生成，不再进行检查</b>
     */
    public abstract DrawHolder read(CompoundTag value);

    /**
     * 将图片序列化成 String 并存入 StringBuffer 中
     */
    public abstract void write(DrawHolder holder, StringBuffer sb);

    /**
     * 将图片存入 buffer 中
     */
    public abstract void write(DrawHolder holder, FriendlyByteBuf buf);

    /**
     * 将图片存入 nbt 中
     */
    public abstract void write(DrawHolder holder, CompoundTag tag);

    /**
     * 将图片生成 NativeImage，主要用于导出到文件
     */
    public abstract Supplier<NativeImage> toImage(DrawHolder holder);

    /**
     * 创建一个用于保存图片的容器
     */
    public abstract DrawHolder newDraw();

    /**
     * 从该版本上一个版本的图片版本升级到当前版本，默认实现是直接把旧数据传递到新容器
     */
    protected DrawHolder updateFrom(DrawHolder oldHolder) {
        DrawHolder holder = newDraw();
        holder.apply(oldHolder);
        return holder;
    }
}
