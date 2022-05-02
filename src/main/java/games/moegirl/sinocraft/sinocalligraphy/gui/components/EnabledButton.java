package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.client.TextureAtlas;
import games.moegirl.sinocraft.sinocalligraphy.gui.GLSwitcher;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class EnabledButton extends Button {

    private final String name;
    private final TextureAtlas atlas;
    private boolean isEnable = false;

    public EnabledButton(AbstractContainerScreen<?> parent, int pWidth, int pHeight, TextureAtlas alias, String name,
                         String hoverKey, Consumer<EnabledButton> pOnPress) {
        super(0, 0, pWidth, pHeight, TextComponent.EMPTY, pButton -> {
            if (((EnabledButton) pButton).isEnable()) {
                pOnPress.accept((EnabledButton) pButton);
            }
        }, (pButton, pPoseStack, pMouseX, pMouseY) ->
                parent.renderTooltip(pPoseStack, new TranslatableComponent(hoverKey), pMouseX, pMouseY));
        this.name = name;
        this.atlas = alias;
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        String n = isEnable() ? isHoveredOrFocused() ? name + "_hover" : name : name + "_disable";
        atlas.blit(pPoseStack, n, x, y, width, height, GLSwitcher.blend().enable(), GLSwitcher.depth().enable());

        if (this.isHoveredOrFocused()) {
            this.renderToolTip(pPoseStack, pMouseX, pMouseY);
        }
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public EnabledButton resize(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
