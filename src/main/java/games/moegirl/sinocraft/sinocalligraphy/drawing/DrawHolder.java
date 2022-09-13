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
 * 保存了图片的
 */
public interface DrawHolder {

    /**
     * Parse string to a holder
     * @param value string from {@link DrawVersion#write(DrawHolder, StringBuffer)}
     * @return holder
     */
    static Optional<DrawHolder> parse(String value) {
//        return Optional.of(DrawVersion.from(value));
        return DrawVersion.find(value).map(v -> v.read(value)).map(DrawVersion::update);
    }

    /**
     * Parse network buffer to a holder
     * @param value buffer from {@link DrawVersion#write(DrawHolder, FriendlyByteBuf)}
     * @return holder
     */
    static Optional<DrawHolder> parse(FriendlyByteBuf value) {
//        return Optional.of(DrawVersion.from(value));
        return DrawVersion.find(new FriendlyByteBuf(value.copy()))
                .map(v -> v.read(value))
                .map(DrawVersion::update);
    }

    /**
     * Parse nbt to a holder
     * @param value tag from {@link DrawVersion#write(DrawHolder, CompoundTag)}
     * @return holder
     */
    static Optional<DrawHolder> parse(@Nullable CompoundTag value) {
//        return Optional.of(DrawVersion.from(value));x
        return DrawVersion.find(value).map(v -> v.read(value)).map(DrawVersion::update);
    }

    String KEY_NO_AUTHOR = SinoCalligraphy.MODID + ".hover.author.empty";
    Component DEFAULT_AUTHOR = new TranslatableComponent(KEY_NO_AUTHOR);

    /**
     * Get the draw in the holder
     * @return draw
     */
    byte[] getData();

    /**
     * Set a draw to the holder
     * @param data draw data
     */
    void setDraw(byte[] data);

    /**
     * Get the name of author, or unknown author if not exist author
     * @return author name
     */
    Component getAuthor();

    /**
     * Set the name of author
     * @param author author name
     */
    void setAuthor(@Nullable Component author);

    /**
     * Set an author name from entity
     * @param author author
     */
    default void setAuthor(@Nullable Entity author) {
        setAuthor(author == null ? null : author.getDisplayName());
    }

    /**
     * Set an author name from string
     * @param author author
     */
    default void setAuthor(@Nullable String author) {
        setAuthor(author == null ? null : Component.Serializer.fromJson(author));
    }

    /**
     * True if the draw has an author;
     * @return true if the draw has an author
     */
    boolean hasAuthor();

    /**
     * Get author as string
     * @return author string
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
     * True if the draw is blank (or empty)
     * @return draw is blank
     */
    boolean isEmpty();

    /**
     * Copy another holder to this
     * @param another another draw holder
     * @return true if copy succeed
     */
    default boolean apply(DrawHolder another) {
        if (getClass() == another.getClass()) {
            copyDirectly(another, this);
            return true;
        }
        return false;
    }

    /**
     * Copy data from one holder to another directly
     * @param src source
     * @param dst destination
     */
    static void copyDirectly(DrawHolder src, DrawHolder dst) {
        dst.setDraw(src.getData());
        if (src.hasAuthor()) {
            dst.setAuthor(src.getAuthor());
        }
    }

    static Optional<DrawHolder> from(String str) {
        return DrawVersion.from(str).map(v -> v.read(str)).map(DrawVersion::update);
    }
}
