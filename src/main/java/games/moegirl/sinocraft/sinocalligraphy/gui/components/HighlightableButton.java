package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocore.utility.render.UVOffsetInt;
import games.moegirl.sinocraft.sinocore.utility.render.XYPointInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HighlightableButton extends Button {
    protected Screen gui;

    protected ResourceLocation texture;

    protected UVOffsetInt uncover;
    protected UVOffsetInt hovered;

    public HighlightableButton(XYPointInt location, int width, int height,
                               Component message, OnPress onPress, Screen screen,
                               UVOffsetInt uncoverUV, UVOffsetInt hoveredUV) {
        super(location.x, location.y, width, height, message, onPress);

        gui = screen;

        uncover = uncoverUV;
        hovered = hoveredUV;
    }

    @Override
    public void renderToolTip(PoseStack stack, int mouseX, int mouseY) {
        super.renderToolTip(stack, mouseX, mouseY);

        gui.renderTooltip(stack, getMessage(), mouseX, mouseY);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        isHovered = mouseX >= x
                && mouseY >= y
                && mouseX < x + width
                && mouseY < y + height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        if (!isHovered) {
            blit(stack, x, y, uncover.u, uncover.v, width, height);
        } else {
            blit(stack, x, y, hovered.u, hovered.v, width, height);
        }
    }
}
