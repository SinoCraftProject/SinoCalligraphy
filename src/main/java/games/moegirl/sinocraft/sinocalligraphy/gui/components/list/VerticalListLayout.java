package games.moegirl.sinocraft.sinocalligraphy.gui.components.list;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class VerticalListLayout<T, LIST extends SelectionList<T>> extends SelectionLayout<T> {

    private final int width, height;

    public VerticalListLayout(LIST list, int width, int height) {
        super(list);
        this.width = width;
        this.height = height;
    }

    @Override
    public void measureSize(SelectionEntry<T> entry) {
        entry.setSize(width, height);
    }

    @Override
    public void layoutItem(SelectionEntry<T> entry, @Nullable SelectionEntry<T> previous, @Nullable SelectionEntry<T> next) {
        if (previous == null) {
            entry.setPositionInCanvas(0, 0);
        } else {
            entry.setPositionInCanvas(previous.getXInCanvas(), previous.getYInCanvas() + previous.getHeight());
        }
    }
}
