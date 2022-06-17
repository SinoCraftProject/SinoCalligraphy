package games.moegirl.sinocraft.sinocalligraphy.drawing;

public class DrawVersions {

    public static final DrawVersion BRUSH_V1 = new BrushV1Version();

    public static final DrawVersion BRUSH_V2 = new BrushV2Version();

    /**
     * Latest and default version for brush
     */
    public static final DrawVersion LATEST_BRUSH_VERSION = BRUSH_V2;

    public static void register() {
        DrawVersion.register(BRUSH_V1);
        DrawVersion.register(BRUSH_V2);
    }
}
