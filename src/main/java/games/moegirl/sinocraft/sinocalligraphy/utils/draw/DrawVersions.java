package games.moegirl.sinocraft.sinocalligraphy.utils.draw;

import java.lang.reflect.Field;

public class DrawVersions {

    public static final DrawVersion BRUSH_V1 = new BrushV1Version();

    public static final DrawVersion BRUSH_V2 = new BrushV2Version();

    public static void addAll() {
        for (Field field : DrawVersions.class.getFields()) {
            if (field.getType() == DrawVersion.class) {
                try {
                    DrawVersion.register((DrawVersion) field.get(null));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
