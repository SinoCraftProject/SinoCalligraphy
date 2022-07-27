package games.moegirl.sinocraft.sinocalligraphy.utility;

import net.minecraft.util.FastColor;
import oshi.util.tuples.Quartet;

import java.util.HashMap;
import java.util.Map;

public class DrawHelper {
    public static final Map<Integer, Integer> INVERT_MAP = new HashMap<>(256);

    static {
        for (int i = 0; i < 256; i++) {
            INVERT_MAP.put(i, 255 - i);
        }
    }

    public static int mix(int foreground, int background, int alpha) {
        if (FastColor.ARGB32.alpha(background) != 255) {
            int tmp = foreground;
            foreground = background;
            background = tmp;
        }

        var aRed = FastColor.ARGB32.red(foreground);
        var aGreen = FastColor.ARGB32.green(foreground);
        var aBlue = FastColor.ARGB32.blue(foreground);
        var aAlpha = FastColor.ARGB32.alpha(foreground);
        var bRed = FastColor.ARGB32.red(background);
        var bGreen = FastColor.ARGB32.green(background);
        var bBlue = FastColor.ARGB32.blue(background);
        var bAlpha = FastColor.ARGB32.alpha(background);

        return mix(aRed, aGreen, aBlue, aAlpha, bRed, bGreen, bBlue, bAlpha, alpha);
    }

    public static int mix(int foreground, int background, float alpha) {
        // Todo: qyl27: test and adjust.
        var aRed = FastColor.ARGB32.red(foreground);
        var aGreen = FastColor.ARGB32.green(foreground);
        var aBlue = FastColor.ARGB32.blue(foreground);

        return FastColor.ARGB32.color(clamp(alpha), clamp(aRed), clamp(aGreen), clamp(aBlue));
    }

    public static int mix(int aRed, int aGreen, int aBlue, int aAlpha, int bRed, int bGreen, int bBlue, int bAlpha, float alpha) {
        // Todo: qyl27: test and adjust.
        return FastColor.ARGB32.color(clamp(alpha), clamp(aRed), clamp(aGreen), clamp(aBlue));
    }

    public static int clamp(float v) {
        return Math.min((int) v, 255);
    }

    public static int singleInvert(int c) {
        return INVERT_MAP.get(c);
    }

    public static int invert(int color) {
        var a = INVERT_MAP.get(FastColor.ARGB32.alpha(color));
        var r = INVERT_MAP.get(FastColor.ARGB32.red(color));
        var g = INVERT_MAP.get(FastColor.ARGB32.green(color));
        var b = INVERT_MAP.get(FastColor.ARGB32.blue(color));
        return FastColor.ARGB32.color(a, r, g, b);
    }

    public static int toABGR(int rgba) {
        var a = FastColor.ARGB32.alpha(rgba);
        var r = FastColor.ARGB32.red(rgba);
        var g = FastColor.ARGB32.green(rgba);
        var b = FastColor.ARGB32.blue(rgba);

        return FastColor.ARGB32.color(a, b, g, r);
    }

    public static Quartet<Integer, Integer, Integer, Integer> toRGBA(int color) {
        var a = FastColor.ARGB32.alpha(color);
        var r = FastColor.ARGB32.red(color);
        var g = FastColor.ARGB32.green(color);
        var b = FastColor.ARGB32.blue(color);

        return new Quartet<>(r, g, b, a);
    }

    public static Quartet<Float, Float, Float, Float> toGlRGBA(int color) {
        color = invert(color);

        var a = (float) (FastColor.ARGB32.alpha(color) * 0.00390625);
        var r = (float) (FastColor.ARGB32.red(color) * 0.00390625);
        var g = (float) (FastColor.ARGB32.green(color) * 0.00390625);
        var b = (float) (FastColor.ARGB32.blue(color) * 0.00390625);

        return new Quartet<>(r, g, b, a);
    }
}
