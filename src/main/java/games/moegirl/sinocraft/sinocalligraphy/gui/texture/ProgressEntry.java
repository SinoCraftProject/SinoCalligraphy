package games.moegirl.sinocraft.sinocalligraphy.gui.texture;

import javax.annotation.Nullable;

public record ProgressEntry(String name, int x, int y, @Nullable String texture, String textureFilled, boolean isVertical, boolean isOpposite) {
}
