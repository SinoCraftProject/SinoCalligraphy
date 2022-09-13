package games.moegirl.sinocraft.sinocalligraphy.drawing;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.drawing.version.DrawVersion;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * 保存图片本身数据及其作者信息
 */
public interface DrawHolder {

    /**
     * 从字符串读图片
     */
    static Optional<DrawHolder> parse(String value, DrawVersion target) {
        return DrawVersion.find(value, target).map(v -> v.read(value)).map(DrawVersion::update);
    }

    /**
     * 从网络读图片
     */
    static Optional<DrawHolder> parse(FriendlyByteBuf value, DrawVersion target) {
        return DrawVersion.find(value, target).map(v -> v.read(value)).map(DrawVersion::update);
    }

    /**
     * 从 NBT 数据读图片
     */
    static Optional<DrawHolder> parse(CompoundTag value, DrawVersion target) {
//        return Optional.of(DrawVersion.from(value));x
        return DrawVersion.find(value, target).map(v -> v.read(value)).map(DrawVersion::update);
    }

    String KEY_NO_AUTHOR = SinoCalligraphy.MODID + ".hover.author.empty";
    Component DEFAULT_AUTHOR = new TranslatableComponent(KEY_NO_AUTHOR);

    /**
     * 获取图片的原始数据
     */
    Object getData();

    /**
     * 设置新的图片数据
     */
    void setDraw(Object data);

    /**
     * 获取作者。该方法永远不应该返回 null
     */
    Component getAuthor();

    /**
     * 设置作者
     */
    void setAuthor(@Nullable Component author);

    /**
     * 设置作者（实体）
     */
    default void setAuthor(@Nullable Entity author) {
        setAuthor(author == null ? null : author.getDisplayName());
    }

    /**
     * 设置作者（名称）
     */
    default void setAuthor(@Nullable String author) {
        setAuthor(author == null ? null : Component.Serializer.fromJson(author));
    }

    /**
     * 是否包含作者信息
     */
    boolean hasAuthor();

    /**
     * 以 String 形式获取作者信息
     */
    default String getAuthorAsString() {
        return Component.Serializer.toJson(getAuthor());
    }

    /**
     * Get the version of the holder
     * @return version
     */
    DrawVersion getVersion();

    /**
     * 当前画作是否是空白的
     */
    boolean isEmpty();

    /**
     * 将其他画作复制到当前画作中
     * @return true 可以并已经复制成功
     */
    default boolean apply(DrawHolder another) {
        if (getClass() == another.getClass()) {
            copyDirectly(another, this);
            return true;
        }
        return false;
    }

    /**
     * 直接复制图画内容。注意：
     * <ul>
     *     <li>浅复制</li>
     *     <li>不会验证是否匹配</li>
     * </ul>
     */
    static void copyDirectly(DrawHolder src, DrawHolder dst) {
        dst.setDraw(src.getData());
        if (src.hasAuthor()) {
            dst.setAuthor(src.getAuthor());
        }
    }
}
