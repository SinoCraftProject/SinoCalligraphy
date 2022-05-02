package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.client.TextureAtlas;
import games.moegirl.sinocraft.sinocalligraphy.gui.GLSwitcher;
import games.moegirl.sinocraft.sinocore.utility.render.XYPointInt;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HighlightableButton extends Button {
    protected Screen gui;

    protected final TextureAtlas atlas;
    private final String name;

    public HighlightableButton(XYPointInt location, int width, int height,
                               Component message, OnPress onPress, Screen screen, TextureAtlas atlas, String name) {
        super(location.x, location.y, width, height, message, onPress);
        gui = screen;
        this.atlas = atlas;
        this.name = name;
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

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        String n = isHovered ? name + "_light" : name + "_dark";
        atlas.blit(stack, n, x, y, width, height, GLSwitcher.blend().enable(), GLSwitcher.depth().enable());
    }
}
