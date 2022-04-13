package games.moegirl.sinocraft.sinocalligraphy.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.SinoCalligraphy;
import games.moegirl.sinocraft.sinocalligraphy.gui.components.HighlightableButton;
import games.moegirl.sinocraft.sinocalligraphy.gui.menu.BrushMenu;
import games.moegirl.sinocraft.sinocalligraphy.network.SCANetworks;
import games.moegirl.sinocraft.sinocalligraphy.network.packet.DrawC2SPacket;
import games.moegirl.sinocraft.sinocore.utility.render.UVOffsetInt;
import games.moegirl.sinocraft.sinocore.utility.render.XYPointInt;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BrushGuiScreen extends AbstractContainerScreen<BrushMenu> {
    private static final ResourceLocation GUI = new ResourceLocation(SinoCalligraphy.MODID, "textures/gui/chinese_brush.png");
    private Button buttonColorDarker;
    private Button buttonColorLighter;

    public BrushGuiScreen(BrushMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        width = 212;
        height = 236;
    }

    @Override
    protected void init() {
        super.init();

        buttonColorDarker = new HighlightableButton(new XYPointInt(leftPos + 16, topPos + 112),
                11, 7, new TranslatableComponent("sinocraft.sinocalligraphy.gui.button.darker"),
                button -> menu.increaseBrushColor(), this,
                new UVOffsetInt(11, 236), new UVOffsetInt(11, 243));

        buttonColorLighter = new HighlightableButton(new XYPointInt(leftPos + 16, topPos + 112),
                11, 7, new TranslatableComponent("sinocraft.sinocalligraphy.gui.button.lighter"),
                button -> menu.decreaseBrushColor(), this,
                new UVOffsetInt(0, 236), new UVOffsetInt(0, 243));

        addWidget(buttonColorDarker);
        addWidget(buttonColorLighter);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, GUI);
        blit(stack, leftPos, topPos, 0, 0, width, height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonCode) {
        super.mouseClicked(mouseX, mouseY, buttonCode);
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
