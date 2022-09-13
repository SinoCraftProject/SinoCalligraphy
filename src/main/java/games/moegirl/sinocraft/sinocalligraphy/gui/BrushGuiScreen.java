package games.moegirl.sinocraft.sinocalligraphy.gui;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.version.DrawVersions;
import games.moegirl.sinocraft.sinocalligraphy.gui.components.Canvas;
import games.moegirl.sinocraft.sinocalligraphy.gui.components.ColorSelectionList;
import games.moegirl.sinocraft.sinocalligraphy.gui.container.BrushContainer;
import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.network.SCANetworks;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.DrawSaveC2SPacket;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.SaveFailedS2CPacket;
import games.moegirl.sinocraft.sinocalligraphy.utility.XuanPaperType;
import games.moegirl.sinocraft.sinocore.api.client.GLSwitcher;
import games.moegirl.sinocraft.sinocore.api.client.screen.TextureMapClient;
import games.moegirl.sinocraft.sinocore.api.client.screen.component.AnimatedText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu.TEXTURE;

public class BrushGuiScreen extends AbstractContainerScreen<BrushMenu> {
    public static final String KEY_SAVE = SinoCalligraphy.MODID + ".gui.brush.save";
    public static final String KEY_SAVE_SUCCEED = SinoCalligraphy.MODID + ".gui.brush.save.succeed";
    public static final String KEY_SAVE_ERR_INK = SinoCalligraphy.MODID + ".gui.brush.save.err.ink";
    public static final String KEY_SAVE_ERR_PAPER = SinoCalligraphy.MODID + ".gui.brush.save.err.paper";
    public static final String KEY_COPY = SinoCalligraphy.MODID + ".gui.brush.copy";
    public static final String KEY_COPIED = SinoCalligraphy.MODID + ".gui.brush.copied";
    public static final String KEY_PASTE_FAILED = SinoCalligraphy.MODID + ".gui.brush.paste.failed";
    public static final String KEY_PASTE_SUCCEED = SinoCalligraphy.MODID + ".gui.brush.paste.succeed";
    public static final String KEY_OUTPUT = SinoCalligraphy.MODID + ".gui.brush.output";
    public static final String KEY_OUTPUT_SUCCEED = SinoCalligraphy.MODID + ".gui.brush.output.succeed";
    public static final String KEY_OUTPUT_FAILED = SinoCalligraphy.MODID + ".gui.brush.output.failed";
    public static final String KEY_DRAW_EMPTY = SinoCalligraphy.MODID + ".gui.brush.draw.empty";
    public static final String KEY_SAVING = SinoCalligraphy.MODID + ".gui.brush.save.waiting";

    private static final TextureMapClient CLIENT = new TextureMapClient(TEXTURE);

    private final Lazy<Canvas> canvas = Lazy.of(() -> new Canvas(this, CLIENT, "canvas", "shadow", menu::getColor, menu::setColor, XuanPaperType.WHITE));
    private final Lazy<AnimatedText> text = Lazy.of(() -> new AnimatedText(130, 130));
    private final Lazy<ColorSelectionList> list = Lazy.of(() -> ColorSelectionList.create(this));
    private boolean saving = false;

    public BrushGuiScreen(BrushMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        menu.setController(new ClientGuiController());

        width = 212;
        height = 236;

        imageWidth = 212;
        imageHeight = 236;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(canvas.get().resize(leftPos + 58, topPos + 11, 130));
        // qyl27: Ensure the text below the canvas.
        addRenderableOnly(text.get().resize(leftPos + 58 + (130 / 2 - 10), topPos + 11 + 132, font));
        addRenderableWidget(list.get().resize(leftPos, topPos));
        CLIENT.placeButton("copy", this, this::copyDraw, this::pasteDraw, this::addRenderableWidget);
        CLIENT.placeButton("output", this, this::saveToFile, this::addRenderableWidget);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTick);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        CLIENT.blitTexture(stack, "background", this, GLSwitcher.blend().enable(), GLSwitcher.depth().enable());
    }

    @Override
    public void onClose() {
        super.onClose();
        menu.setController(new BrushMenu.GuiController());
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        boolean value = super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        canvas.get().mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        return value;
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        boolean value = super.mouseReleased(pMouseX, pMouseY, pButton);
        canvas.get().mouseReleased(pMouseX, pMouseY, pButton);
        return value;
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        canvas.get().mouseMoved(pMouseX, pMouseY);
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Slot slot;
        if ((slot = findSlot(mouseX, mouseY)) != null
                && slot.container instanceof BrushContainer
                && slot.getContainerSlot() == BrushContainer.FILLED_XUAN_PAPER_SLOT) {
            if (saving) {
                text.get().begin(Duration.ofSeconds(3), 1, 0, 255, 255, new TranslatableComponent(KEY_SAVING));
            } else {
                saving = true;
                text.get().begin(Duration.ofSeconds(3), 1, 0, 255, 255, new TranslatableComponent(KEY_SAVE_SUCCEED));
                SCANetworks.send(new DrawSaveC2SPacket(canvas.get().getDraw(Minecraft.getInstance().player), button));
            }
            return true;
        }
        return superMouseClicked(mouseX, mouseY, button);
    }

    public boolean superMouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Nullable
    private Slot findSlot(double mouseX, double mouseY) {
        for (int i = 0; i < menu.slots.size(); ++i) {
            Slot slot = menu.slots.get(i);
            if (!isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY) || !slot.isActive()) continue;
            return slot;
        }
        return null;
    }

    private void copyDraw(Button button) {
        DrawHolder holder = canvas.get().getDraw(Minecraft.getInstance().player);
        StringBuffer sb = new StringBuffer();
        holder.getVersion().write(holder, sb);
        Minecraft.getInstance().keyboardHandler.setClipboard(sb.toString());
        text.get().begin(Duration.ofSeconds(1), 0, 0, 255, 0, new TranslatableComponent(KEY_COPIED));
    }

    private void pasteDraw(Button button) {
        String data = Minecraft.getInstance().keyboardHandler.getClipboard();
        DrawHolder.parse(data, DrawVersions.LATEST_BRUSH_VERSION)
                .filter(h -> canvas.get().setDraw(h))
                .ifPresentOrElse(c -> {},
                        () -> text.get().begin(Duration.ofSeconds(1), 0, 255, 0, 0, new TranslatableComponent(KEY_PASTE_FAILED, data)));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveToFile(Button button) {
        LocalPlayer player = Minecraft.getInstance().player;
        DrawHolder holder = canvas.get().getDraw(player);
        try (NativeImage image = holder.getVersion().toImage(holder).get()) {
            File name = new File(Minecraft.getInstance().gameDirectory,
                    "sinoseries/sinocalligraphy/draws/" + holder.getAuthor().getString() +
                            "/" + System.currentTimeMillis() + ".png");
            name.getParentFile().mkdirs();
            if (!name.exists()) {
                name.createNewFile();
            }
            image.writeToFile(name);
            if (player != null) {
                player.displayClientMessage(new TranslatableComponent(KEY_OUTPUT_SUCCEED), false);
            }

            TextComponent path = new TextComponent(name.toString());
            path.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, name.getAbsolutePath()));

            if (player != null) {
                player.displayClientMessage(path, false);
            }

        } catch (IOException e) {
            e.printStackTrace();
            if (player != null) {
                player.displayClientMessage(new TranslatableComponent(KEY_OUTPUT_FAILED, e.getMessage()), false);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (mouseX >= canvas.get().x
                && mouseX < (canvas.get().x + 130)
                && mouseY >= canvas.get().y
                && mouseY < (canvas.get().y + 130)) {
            return list.get().mouseScrolled(mouseX, mouseY, delta);
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        canvas.get().keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        canvas.get().keyReleased(keyCode, scanCode, modifiers);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public ColorSelectionList getColorSelection() {
        return list.get();
    }

    public class ClientGuiController extends BrushMenu.GuiController {
        private ClientGuiController() {
        }

        public void handleSaveResult(SaveFailedS2CPacket.Reason result, int button) {
            saving = false;
            switch (result) {
                case NoInk -> text.get().begin(Duration.ofSeconds(2), 0, 255, 0, 0, KEY_SAVE_ERR_INK);
                case NoPaper -> text.get().begin(Duration.ofSeconds(2), 0, 255, 0, 0, KEY_SAVE_ERR_PAPER);
                case Succeed -> {
                    text.get().begin(Duration.ofSeconds(2), 0, 0, 255, 0, KEY_SAVE_SUCCEED);
                    Minecraft mc = Minecraft.getInstance();
                    Window w = mc.getWindow();
                    double x = mc.mouseHandler.xpos() * w.getGuiScaledWidth() / w.getScreenWidth();
                    double y = mc.mouseHandler.ypos() * w.getGuiScaledHeight() / w.getScreenHeight();
                    superMouseClicked(x, y, button);
                }
            }
        }

        @Override
        public void setCanvasEnable(boolean isEnable) {
            canvas.get().setEnable(isEnable);
        }

        @Override
        public void clearCanvas() {
            canvas.get().clear();
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            SCANetworks.send(new DrawSaveC2SPacket(canvas.get().getDraw(player), 0));
            clearCanvas();
            super.onTake(player, stack);
        }

        @Override
        public boolean isEmpty() {
            return canvas.get().isEmpty();
        }
    }
}

