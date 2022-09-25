package games.moegirl.sinocraft.sinocalligraphy.drawing.brush.version;

import java.util.*;

public enum DrawVersions {
    BRUSH_V1(),
    BRUSH_V2(),
    BRUSH_V3()
    ;

    private IDrawVersion drawVersion;

    private DrawVersions(IDrawVersion version) {
        drawVersion = version;
    }

    public IDrawVersion getVersion() {
        return drawVersion;
    }

    public static Set<IDrawVersion> getVersions(DrawType type) {
        var set = new HashSet<IDrawVersion>();

        for (var version : DrawVersions.values()) {
            if (version.getVersion().getType() == type) {
                set.add(version.getVersion());
            }
        }

        return set;
    }

    public static IDrawVersion getLatest(DrawType type) {
        for (var version : DrawVersions.values()) {
            if (version.getVersion().isLatest()) {
                return version.getVersion();
            }
        }

        throw new RuntimeException("There is no latest version for this type, it should not be happened.");
    }
}
