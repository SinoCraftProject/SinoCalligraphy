package games.moegirl.sinocraft.sinocalligraphy.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.gui.components.Canvas;
import games.moegirl.sinocraft.sinocalligraphy.gui.components.NormalButton;
import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.network.SCANetworks;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.DrawSaveC2SPacket;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.SaveFailedS2CPacket;
import games.moegirl.sinocraft.sinocalligraphy.utils.draw.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.utils.draw.DrawVersion;
import games.moegirl.sinocraft.sinocore.api.client.component.AnimatedText;
import games.moegirl.sinocraft.sinocore.api.client.component.EnabledButton;
import games.moegirl.sinocraft.sinocore.api.client.component.HighlightableButton;
import games.moegirl.sinocraft.sinocore.api.utility.GLSwitcher;
import games.moegirl.sinocraft.sinocore.api.utility.TextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

@OnlyIn(Dist.CLIENT)
public class BrushGuiScreen extends AbstractContainerScreen<BrushMenu> {
    public static final ResourceLocation GUI = new ResourceLocation(SinoCalligraphy.MODID, "textures/gui/chinese_brush.png");
    public static final TextureAtlas ATLAS = new TextureAtlas(GUI, 512, 256)
            .add("background", 0, 0, 212, 236)
            .add("save", 38, 236, 14, 14)
            .add("save_disable", 24, 236, 14, 14)
            .add("save_hover", 52, 236, 14, 14)
            .add("canvas", 212, 0, 130, 130)
            .add("canvas_disable", 342, 0, 130, 130)
            .add("up_dark", 11, 236, 11, 7)
            .add("up_light", 11, 243, 11, 7)
            .add("down_dark", 0, 236, 11, 7)
            .add("down_light", 0, 243, 11, 7)
            .add("copy", 78, 236, 12, 14)
            .add("output", 66, 236, 12, 14);

    public static final String KEY_SAVE = SinoCalligraphy.MODID + ".gui.brush.save";
    public static final String KEY_SAVE_SUCCEED = SinoCalligraphy.MODID + ".gui.brush.save.succeed";
    public static final String KEY_SAVE_ERR_INK = SinoCalligraphy.MODID + ".gui.brush.save.err.ink";
    public static final String KEY_SAVE_ERR_PAPER = SinoCalligraphy.MODID + ".gui.brush.save.err.paper";
    public static final String KEY_COPY = SinoCalligraphy.MODID + ".gui.brush.copy";
    public static final String KEY_COPIED = SinoCalligraphy.MODID + ".gui.brush.copied";
    public static final String KEY_PASTE_FAILED = SinoCalligraphy.MODID + ".gui.brush.paste.failed";
    public static final String KEY_OUTPUT = SinoCalligraphy.MODID + ".gui.brush.output";
    public static final String KEY_OUTPUT_SUCCEED = SinoCalligraphy.MODID + ".gui.brush.output.succeed";
    public static final String KEY_OUTPUT_FAILED = SinoCalligraphy.MODID + ".gui.brush.output.failed";

    private final Lazy<EnabledButton> save = Lazy.of(() -> new EnabledButton(this, 14, 14, ATLAS, "save", KEY_SAVE, this::saveToServer));
    private final Lazy<Canvas> canvas = Lazy.of(() -> new Canvas(ATLAS, "canvas", menu::getColor, menu::setColor));
    private final Lazy<AnimatedText> text = Lazy.of(() -> new AnimatedText(130, 130));
    private final Lazy<NormalButton> copy = Lazy.of(() -> new NormalButton(this, 14, 14, ATLAS, "copy", KEY_COPY, this::copyDraw, this::pasteDraw));
    private final Lazy<NormalButton> output = Lazy.of(() -> new NormalButton(this, 14, 14, ATLAS, "output", KEY_OUTPUT, this::saveToFile));

    public BrushGuiScreen(BrushMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        pMenu.gui = new ClientGuiController();

        width = 212;
        height = 236;

        imageWidth = 212;
        imageHeight = 236;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new HighlightableButton(new Point(leftPos + 16, topPos + 112),
                11, 7, new TranslatableComponent("sinocraft.sinocalligraphy.gui.button.darker"),
                button -> menu.increaseBrushColor(), this, ATLAS, "up"));
        addRenderableWidget(new HighlightableButton(new Point(leftPos + 16, topPos + 166),
                11, 7, new TranslatableComponent("sinocraft.sinocalligraphy.gui.button.lighter"),
                button -> menu.decreaseBrushColor(), this, ATLAS, "down"));
        addRenderableWidget(save.get().resize(leftPos + 15, topPos + 184));
        addRenderableWidget(canvas.get().resize(leftPos + 58, topPos + 11, 130));
        addRenderableWidget(copy.get().resize(leftPos + 15, topPos + 168));
        addRenderableWidget(output.get().resize(leftPos + 15, topPos + 152));
        addRenderableOnly(text.get().resize(leftPos + 94, topPos + 121, font));
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
        ATLAS.blit(stack, "background", leftPos, topPos, imageWidth, imageHeight,
                GLSwitcher.blend().enable(),
                GLSwitcher.depth().enable());
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        font.draw(stack, Integer.toString(menu.getColor()), 18, 139, 0xffffff);
    }

    @Override
    public void onClose() {
        super.onClose();
        menu.gui = new BrushMenu.GuiController();
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

    private void saveToServer(EnabledButton button) {
        SCANetworks.send(new DrawSaveC2SPacket(canvas.get().getDraw(Minecraft.getInstance().player)));
        button.setEnable(false);
    }

    private void copyDraw(NormalButton button) {
        DrawHolder holder = canvas.get().getDraw(Minecraft.getInstance().player);
        StringBuffer sb = new StringBuffer();
        holder.version().write(holder, sb);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sb.toString()), null);
        text.get().begin(Duration.ofSeconds(1), 0, Color.GREEN, new TranslatableComponent(KEY_COPIED));
    }

    private void pasteDraw(NormalButton button) {
        Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        DataFlavor flavor = DataFlavor.stringFlavor;
        try {
            String data = (String) contents.getTransferData(flavor);
            DrawHolder.parse(data)
                    .map(DrawVersion::update)
                    .filter(h -> canvas.get().setDraw(h))
                    .ifPresentOrElse(c -> {},
                            () -> text.get().begin(Duration.ofSeconds(1), 0, Color.RED, new TranslatableComponent(KEY_PASTE_FAILED, data)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToFile(NormalButton button) {
        DrawHolder holder = canvas.get().getDraw(Minecraft.getInstance().player);
        BufferedImage image = holder.version().toImage(holder);
        try {
            File name = new File(Minecraft.getInstance().gameDirectory, SinoCalligraphy.MODID + "/output/" + System.currentTimeMillis() + ".png");
            name.getParentFile().mkdirs();
            if (!name.exists()) {
                name.createNewFile();
            }
            ImageIO.write(image, "png", name);
            Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent(KEY_OUTPUT_SUCCEED), false);
            TextComponent path = new TextComponent(name.toString());
            path.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, name.getAbsolutePath()));
            Minecraft.getInstance().player.displayClientMessage(path, false);
        } catch (IOException e) {
            e.printStackTrace();
            Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent(KEY_OUTPUT_FAILED, e.getMessage()), false);
        }
    }

    public class ClientGuiController extends BrushMenu.GuiController {
        private ClientGuiController() {
        }

        public void handleSaveResult(SaveFailedS2CPacket.Reason code) {
            save.get().setEnable(true);
            switch (code) {
                case Succeed -> text.get().begin(Duration.ofSeconds(2), 0, Color.GREEN, KEY_SAVE_SUCCEED);
                case NoInk -> text.get().begin(Duration.ofSeconds(2), 0, Color.RED, KEY_SAVE_ERR_INK);
                case NoPaper -> text.get().begin(Duration.ofSeconds(2), 0, Color.RED, KEY_SAVE_ERR_PAPER);
            }
        }

        @Override
        public void setCanvasEnable(boolean isEnable) {
            canvas.get().setEnable(isEnable);
            save.get().setEnable(isEnable);
        }

        @Override
        public void clearCanvas() {
            canvas.get().clear();
        }
    }
}

