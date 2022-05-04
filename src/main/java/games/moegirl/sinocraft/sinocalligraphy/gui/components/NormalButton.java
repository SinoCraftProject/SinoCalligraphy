package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocore.api.utility.GLSwitcher;
import games.moegirl.sinocraft.sinocore.api.utility.TextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.Nullable;

public class NormalButton extends Button {

    private final TextureAtlas atlas;
    private final String name;
    @Nullable
    private final OnPress onRightClick;

    public NormalButton(AbstractContainerScreen<?> parent, int width, int height, TextureAtlas atlas, String name, String hover, OnPress pOnPress) {
        this(parent, width, height, atlas, name, hover, pOnPress, null);
    }

    public NormalButton(AbstractContainerScreen<?> parent, int width, int height, TextureAtlas atlas, String name, String hover, OnPress pLeftClick, @Nullable OnPress pRightClick) {
        super(0, 0, width, height, TextComponent.EMPTY, pLeftClick,
                (pButton, pPoseStack, pMouseX, pMouseY) -> parent.renderTooltip(pPoseStack, new TranslatableComponent(hover), pMouseX, pMouseY));
        this.atlas = atlas;
        this.name = name;
        this.onRightClick = pRightClick;
    }

    public NormalButton resize(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.visible) {
            if (!this.isValidClickButton(pButton)) {
                if (onRightClick != null) {
                    onRightClick.press(this);
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    return true;
                }
            }
            return false;
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        atlas.blit(pPoseStack, name, x, y, width, height,
                GLSwitcher.depth().enable(), GLSwitcher.blend().enable());
        if (this.isHovered) {
            this.renderToolTip(pPoseStack, pMouseX, pMouseY);
        }
    }

    public interface OnPress extends Button.OnPress {
        void press(NormalButton button);

        @Override
        default void onPress(Button pButton) {
            press((NormalButton) pButton);
        }
    }
}
