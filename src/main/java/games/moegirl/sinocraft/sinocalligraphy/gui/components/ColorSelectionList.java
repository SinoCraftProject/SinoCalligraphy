package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public class ColorSelectionList extends AbstractSelectionList<ColorSelectionList.ColorEntry> {

    public ColorSelectionList(int pWidth, int pHeight, int pY0, int pY1, int pItemHeight) {
        super(Minecraft.getInstance(), pWidth, pHeight, pY0, pY1, pItemHeight);
    }

    protected ColorSelectionList add(ColorEntry pEntry) {
        addEntry(pEntry);
        return this;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, "Color List");
    }

    public static class ColorEntry extends Entry<ColorEntry> {

        public final int color;

        public ColorEntry(int color) {
            this.color = color;
        }

        @Override
        public void render(PoseStack pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
            
        }
    }
}
