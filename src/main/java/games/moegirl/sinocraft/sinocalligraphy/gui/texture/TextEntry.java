package games.moegirl.sinocraft.sinocalligraphy.gui.texture;

import javax.annotation.Nullable;

public record TextEntry(String name, int x, int y, int color, @Nullable String text, @Nullable String rawText,
                        boolean shadow, boolean center) {
}
