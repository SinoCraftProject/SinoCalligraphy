package games.moegirl.sinocraft.sinocalligraphy.utils.draw;

import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * A holder with version and draw data
 */
public interface DrawHolder {

    /**
     * Parse string to a holder
     * @param value string from {@link DrawVersion#write(DrawHolder, StringBuffer)}
     * @return holder
     */
    static Optional<DrawHolder> parse(String value) {
        return DrawVersion.find(value).map(v -> v.read(value)).map(DrawVersion::update);
    }

    /**
     * Parse network buffer to a holder
     * @param value buffer from {@link DrawVersion#write(DrawHolder, FriendlyByteBuf)}
     * @return holder
     */
    static Optional<DrawHolder> parse(FriendlyByteBuf value) {
        return DrawVersion.find(value).map(v -> v.read(value)).map(DrawVersion::update);
    }

    /**
     * Parse nbt to a holder
     * @param value tag from {@link DrawVersion#write(DrawHolder, CompoundTag)}
     * @return holder
     */
    static Optional<DrawHolder> parse(@Nullable CompoundTag value) {
        return DrawVersion.find(value).map(v -> v.read(value)).map(DrawVersion::update);
    }

    String KEY_NO_AUTHOR = SinoCalligraphy.MODID + ".hover.author.empty";
    Component DEFAULT_AUTHOR = new TranslatableComponent(KEY_NO_AUTHOR);

    /**
     * Get the draw in the holder
     * @return draw
     */
    Object data();

    /**
     * Set a draw to the holder
     * @param data draw data
     */
    void setDraw(Object data);

    /**
     * Get the name of author, or unknown author if not exist author
     * @return author name
     */
    Component author();

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
    default String authorAsString() {
        return Component.Serializer.toJson(author());
    }

    /**
     * Get the version of the holder
     * @return version
     */
    DrawVersion version();

    /**
     * True if the draw is blank (or empty)
     * @return draw is blank
     */
    boolean isEmpty();

    /**
     * Get an object to draw the draw
     * @return render object
     */
    @OnlyIn(Dist.CLIENT)
    DrawRender render();

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
        dst.setDraw(src.data());
        if (src.hasAuthor()) {
            dst.setAuthor(src.author());
        }
    }
}
