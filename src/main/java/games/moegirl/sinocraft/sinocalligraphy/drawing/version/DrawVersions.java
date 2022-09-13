package games.moegirl.sinocraft.sinocalligraphy.drawing.version;

public class DrawVersions {
    public static final DrawVersion BRUSH_V1 = new VersionBrush1();

    public static final DrawVersion BRUSH_V2 = new VersionBrush2();

    public static final DrawVersion BRUSH_V3_WHITE = new VersionBrush3White();

    /**
     * Latest and default version for brush
     */
    public static final DrawVersion LATEST_BRUSH_VERSION = BRUSH_V3_WHITE;

    /**
     * 调用保证所有版本初始化
     */
    public static void register() {
    }
}
