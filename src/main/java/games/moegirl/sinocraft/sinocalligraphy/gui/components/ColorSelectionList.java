package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jetbrains.annotations.Nullable;

public class ColorSelectionList extends AbstractSelectionList<ColorSelectionList.ColorEntry> {
    public ColorSelectionList(int width, int height, int y0, int y1, int itemHeight) {
        super(Minecraft.getInstance(), width, height, y0, y1, itemHeight);

        for (var i = 0; i < 16; i++) {
            add(new ColorEntry(i, this));
        }
    }

    protected ColorSelectionList add(ColorEntry entry) {
        addEntry(entry);
        return this;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, "Color List");
    }

    @Nullable
    @Override
    public ColorEntry getFocused() {
        return super.getFocused();
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        super.setFocused(focused);
    }

    public static class ColorEntry extends Entry<ColorEntry> {
        public final ColorSelectionList parentList;
        public final int colorValue;

        public ColorEntry(int color, ColorSelectionList parent) {
            colorValue = color;
            parentList = parent;
        }

        @Override
        public void render(PoseStack stack, int index, int top, int left, int width, int height,
                           int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            stack.pushPose();

        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            parentList.setFocused(this);
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
