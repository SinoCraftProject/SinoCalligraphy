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

public interface DrawHolder {

    DrawHolder DEFAULT_FOR_XUAN_PAPER = new BrushV2Holder();

    static Optional<DrawHolder> parse(String value) {
        return DrawVersion.find(value).map(v -> v.read(value));
    }

    static Optional<DrawHolder> parse(FriendlyByteBuf value) {
        return DrawVersion.find(value).map(v -> v.read(value));
    }

    static Optional<DrawHolder> parse(@Nullable CompoundTag value) {
        return DrawVersion.find(value).map(v -> v.read(value));
    }

    String KEY_NO_AUTHOR = SinoCalligraphy.MODID + ".hover.author.empty";
    Component DEFAULT_AUTHOR = new TranslatableComponent(KEY_NO_AUTHOR);

    Object draw();

    void setDraw(Object draw);

    Component author();

    void setAuthor(@Nullable Component author);

    default void setAuthor(@Nullable Entity entity) {
        setAuthor(entity == null ? null : entity.getDisplayName());
    }

    default void setAuthor(@Nullable String author) {
        setAuthor(author == null ? null : Component.Serializer.fromJson(author));
    }

    boolean hasAuthor();

    default String authorAsString() {
        return Component.Serializer.toJson(author());
    }

    DrawVersion version();

    boolean isEmpty();

    void clear();

    @OnlyIn(Dist.CLIENT)
    DrawRender render();

    static void copyDirectly(DrawHolder from, DrawHolder to) {
        to.setDraw(from.draw());
        if (from.hasAuthor()) {
            to.setAuthor(from.author());
        }
    }
}
