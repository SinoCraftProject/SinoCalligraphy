package games.moegirl.sinocraft.sinocalligraphy.gui.texture;

import com.google.common.collect.Iterators;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocore.api.utility.GLSwitcher;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public final class TextureMap {
    private final ResourceLocation texture;
    int width = 0;
    int height = 0;
    private final Entry<PointEntry> points = new Entry<>();
    private final Entry<TextEntry> texts = new Entry<>();
    private final Entry<TextureEntry> textures = new Entry<>();
    private final Entry<SlotEntry> slot = new Entry<>();
    private final Entry<SlotsEntry> slots = new Entry<>();
    private final Entry<ProgressEntry> progress = new Entry<>();
    private final Entry<ButtonEntry> buttons = new Entry<>();

    TextureMap(ResourceLocation texture) {
        this.texture = texture;
    }

    public static TextureMap of(ResourceLocation texture) {
        return TextureParser.parse(texture);
    }

    public static TextureMap of(String modid, String path, String name) {
        return TextureParser.parse(new ResourceLocation(modid, "textures/" + path + "/" + name + ".png"));
    }

    public <T extends Slot, C extends Container> Optional<T> placeSlot(C container, String name, int index, Function<Slot, Slot> addSlot, SlotStrategy<T, C> slot) {
        return this.slot.get(name)
                .map(entry -> placeSlot(container, entry, index, addSlot, slot));
    }

    public void placeSlots(Container container, String name, int beginIndex, Function<Slot, Slot> addSlot, SlotStrategy<Slot, Container> slot) {
        this.slots.get(name).ifPresent(slotsEntry -> {
            int i = beginIndex;
            for (SlotEntry entry : slotsEntry.entries()) {
                placeSlot(container, entry, i++, addSlot, slot);
            }
        });
    }

    private <T extends Slot, C extends Container> T placeSlot(C container, SlotEntry entry, int index, Function<Slot, Slot> addSlot, SlotStrategy<T, C> slot) {
        T s = slot.createSlot(container, index, entry.x(), entry.y());
        addSlot.apply(s);
        return s;
    }

    @OnlyIn(Dist.CLIENT)
    public Optional<Button> placeButton(String name,
                                        AbstractContainerScreen<?> parent,
                                        Button.OnPress onPress,
                                        @Nullable Button.OnPress onRightPress,
                                        Function<Button, Button> addRenderableWidget) {
        return buttons.get(name)
                .map(e -> addRenderableWidget.apply(new ImageButton(parent, this, e, onPress, onRightPress)));
    }

    @OnlyIn(Dist.CLIENT)
    public Optional<Button> placeButton(String name,
                                        AbstractContainerScreen<?> parent,
                                        Button.OnPress onPress,
                                        Function<Button, Button> addRenderableWidget) {
        return placeButton(name, parent, onPress, null, addRenderableWidget);
    }

    @OnlyIn(Dist.CLIENT)
    private void renderText(PoseStack stack, Font font, String name) {
        texts.get(name).ifPresent(entry -> {
            if (entry.text() == null) {
                if (entry.rawText() != null) {
                    int tx = entry.x();
                    int ty = entry.y();
                    String text = entry.rawText();
                    if (entry.center()) {
                        tx += font.width(text) / 2;
                    }
                    if (entry.shadow()) {
                        font.drawShadow(stack, text, tx, ty, entry.color());
                    } else {
                        font.draw(stack, text, tx, ty, entry.color());
                    }
                }
            } else {
                int tx = entry.x();
                int ty = entry.y();
                TranslatableComponent text = new TranslatableComponent(entry.text());
                if (entry.center()) {
                    tx += font.width(text) / 2;
                }
                if (entry.shadow()) {
                    font.drawShadow(stack, text, tx, ty, entry.color());
                } else {
                    font.draw(stack, text, tx, ty, entry.color());
                }
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void bindTexture() {
        RenderSystem.setShaderTexture(0, texture);
    }

    @OnlyIn(Dist.CLIENT)
    public void blitTexture(PoseStack stack, String name,
                            AbstractContainerScreen<?> gui,
                            GLSwitcher... configurations) {
        textures.get(name).ifPresent(entry -> {
            bindTexture();
            blitTexture(stack,
                    gui.getGuiLeft() + entry.x(), gui.getGuiTop() + entry.y(), entry.w(), entry.h(),
                    entry.u(), entry.v(), entry.tw(), entry.th());
        });
        resumeGL(configurations);
    }

    @OnlyIn(Dist.CLIENT)
    public void blitProgress(PoseStack stack, String name,
                             AbstractContainerScreen<?> gui,
                             float progress, GLSwitcher... configurations) {
        this.progress.get(name).ifPresent(entry -> {
            bindTexture();
            int x = gui.getGuiLeft() + entry.x();
            int y = gui.getGuiTop() + entry.y();
            String begin = entry.texture();
            int finalX = x;
            int finalY = y;
            textures.get(begin).ifPresent(bg ->
                    blitTexture(stack, finalX, finalY, bg.w(), bg.h(), bg.u(), bg.v(), bg.tw(), bg.th()));
            if (progress > 0) {
                TextureEntry p = textures.ensureGet(entry.textureFilled());
                int w = p.w(), h = p.h();
                float tu = p.u(), tv = p.v();
                int tw = p.tw(), th = p.th();
                if (entry.isVertical()) {
                    if (entry.isOpposite()) {
                        tv += (1 - progress) * p.th();
                        y += (1 - progress) * p.y();
                    }
                    th = (int) (th * progress);
                } else {
                    if (entry.isOpposite()) {
                        tu += (1 - progress) * p.tw();
                        x += (1 - progress) * p.w();
                    }
                    tw = (int) (tw * progress);
                }
                blitTexture(stack, x, y, w, h, tu, tv, tw, th);
            }
        });
        resumeGL(configurations);
    }

    @OnlyIn(Dist.CLIENT)
    private void blitTexture(PoseStack stack, int x, int y, int w, int h, float tu, float tv, int tw, int th) {
        if (w > 0 && h > 0) {
            GuiComponent.blit(stack, x, y, w, h, tu, tv, tw, th, width, height);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void resumeGL(GLSwitcher... configurations) {
        for (var switcher : configurations) {
            switcher.resume();
        }
    }

    public ResourceLocation texture() {
        return texture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Entry<PointEntry> points() {
        return points;
    }

    public Entry<TextEntry> texts() {
        return texts;
    }

    public Entry<TextureEntry> textures() {
        return textures;
    }

    public Entry<SlotEntry> slot() {
        return slot;
    }

    public Entry<SlotsEntry> slots() {
        return slots;
    }

    public Entry<ProgressEntry> progress() {
        return progress;
    }

    public Entry<ButtonEntry> buttons() {
        return buttons;
    }

    void reload() {
        TextureParser.reload(this);
    }

    public static final class Entry<T> implements Iterable<T> {
        private List<T> list = Collections.emptyList();
        private Map<String, T> map = Collections.emptyMap();

        public Optional<T> get(@Nullable String name) {
            if (name == null || name.isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(map.get(name));
        }

        public T ensureGet(String name) {
            return Objects.requireNonNull(map.get(name));
        }

        public boolean contains(String name) {
            return map.containsKey(name);
        }

        void set(List<T> list, Map<String, T> map) {
            this.list = List.copyOf(list);
            this.map = Map.copyOf(map);
        }

        @Override
        public Iterator<T> iterator() {
            return Iterators.unmodifiableIterator(list.iterator());
        }
    }
}
