package games.moegirl.sinocraft.sinocalligraphy.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.gui.components.HighlightableButton;
import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.network.SCANetworks;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.DrawC2SPacket;
import games.moegirl.sinocraft.sinocore.utility.render.UVOffsetInt;
import games.moegirl.sinocraft.sinocore.utility.render.XYPointInt;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BrushGuiScreen extends AbstractContainerScreen<BrushMenu> {
    public static final int CANVAS_SIZE = 32;
    public static final String PIXELS_TAG_NAME = "pixels";

    private static final ResourceLocation GUI = new ResourceLocation(SinoCalligraphy.MODID, "textures/gui/chinese_brush.png");
    private Button buttonColorDarker;
    private Button buttonColorLighter;

    public BrushGuiScreen(BrushMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        width = 212;
        imageWidth = 212;
        height = 236;
        imageHeight = 236;
    }

    @Override
    protected void init() {
        super.init();

        buttonColorDarker = new HighlightableButton(new XYPointInt(leftPos + 16, topPos + 112),
                11, 7, new TranslatableComponent("sinocraft.sinocalligraphy.gui.button.darker"),
                button -> menu.increaseBrushColor(), this, GUI,
                new UVOffsetInt(11, 236), new UVOffsetInt(11, 243));

        buttonColorLighter = new HighlightableButton(new XYPointInt(leftPos + 16, topPos + 166),
                11, 7, new TranslatableComponent("sinocraft.sinocalligraphy.gui.button.lighter"),
                button -> menu.decreaseBrushColor(), this, GUI,
                new UVOffsetInt(0, 236), new UVOffsetInt(0, 243));

        addRenderableWidget(buttonColorDarker);
        addRenderableWidget(buttonColorLighter);

        super.init();   // Init twice for correct location.
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTick);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        blit(stack, leftPos, topPos, 0, 0, width, height);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        font.draw(stack, Integer.toString(menu.getColor()), 18, 139, 0xffffff);
        draw(stack);
    }

    protected void draw(PoseStack stack) {
        if (menu.getPaper() != ItemStack.EMPTY) {
            ItemStack paper = menu.getFilled();

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, GUI);

            byte[] pixels = null;
            if (paper.getOrCreateTag().contains(PIXELS_TAG_NAME)) {
                pixels = paper.getTag().getByteArray(PIXELS_TAG_NAME);
            } else {
                pixels = new byte[CANVAS_SIZE * CANVAS_SIZE];
            }

            int startX = 61;
            int startY = 14;
            int unit = 128 / CANVAS_SIZE;

            for (int i = 0; i < CANVAS_SIZE; i++) {
                for (int j = 0; j < CANVAS_SIZE; j++) {
                    float color = 0.0625f * (16 - pixels[i * CANVAS_SIZE + j]); // qyl27: For calculating grayscale.
                    RenderSystem.setShaderColor(color, color, color, 1.0f);
                    blit(stack, startX + i * unit, startY + j * unit, 22, 236, unit, unit);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonCode) {
        super.mouseClicked(mouseX, mouseY, buttonCode);
        return mouseDraw(mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int key, double dragX, double dragY) {
        super.mouseDragged(mouseX, mouseY, key, dragX, dragY);
        return mouseDraw(mouseX, mouseY);
    }

    protected boolean mouseDraw(double mouseX, double mouseY) {
        if ((mouseX >= leftPos + 61)
                && (mouseX < leftPos + 61 + 128)
                && (mouseY >= topPos + 14)
                && (mouseY < topPos + 14 + 128)) {
            ItemStack paper = menu.getPaper();

            if (paper == ItemStack.EMPTY) {
                return false;
            }

            int x = (int) (Math.round(mouseX) - leftPos - 61) / 4;
            int y = (int) (Math.round(mouseY) - topPos - 14) / 4;

            SCANetworks.INSTANCE.sendToServer(new DrawC2SPacket(new XYPointInt(x, y), (byte) menu.getColor()));

            return true;
        }
        return false;
    }
}
