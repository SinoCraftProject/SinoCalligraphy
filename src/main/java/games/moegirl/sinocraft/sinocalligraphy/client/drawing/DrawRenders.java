package games.moegirl.sinocraft.sinocalligraphy.client.drawing;

import games.moegirl.sinocraft.sinocalligraphy.drawing.DrawHolder;
import games.moegirl.sinocraft.sinocalligraphy.drawing.version.DrawVersion;
import games.moegirl.sinocraft.sinocalligraphy.drawing.holder.HolderByte32;
import games.moegirl.sinocraft.sinocalligraphy.drawing.version.DrawVersions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DrawRenders {

    private static final Map<DrawVersion, Function<DrawHolder, DrawRender>> RENDER = new HashMap<>();

    public static void register(DrawVersion version, Function<DrawHolder, DrawRender> factory) {
        RENDER.put(version, factory);
    }

    public static DrawRender of(DrawHolder holder) {
        return RENDER.get(holder.getVersion()).apply(holder);
    }

    public static void registerAll() {
        register(DrawVersions.BRUSH_V1, holder -> new RenderGray32((HolderByte32) holder));
        register(DrawVersions.BRUSH_V2, holder -> new RenderGray32((HolderByte32) holder));
        register(DrawVersions.BRUSH_V3_WHITE, holder -> new RenderWhite32((HolderByte32) holder));
    }
}
