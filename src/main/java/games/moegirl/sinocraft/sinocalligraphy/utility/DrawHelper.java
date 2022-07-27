package games.moegirl.sinocraft.sinocalligraphy.utility;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.util.FastColor;
import org.apache.commons.lang3.tuple.Triple;

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
        var a = alpha * 0.00390625;

        return innerMix(foreground, background, (float) a);
    }

    private static int innerMix(int foreground, int background, float alpha) {
        if (FastColor.ARGB32.alpha(background) != 255) {
            int tmp = foreground;
            foreground = background;
            background = tmp;
        }

        return innerMix(foreground, background, alpha, 1 - alpha);
    }

    private static int innerMix(int foreground, int background, float fAlpha, float bAlpha) {
        var a = fAlpha + bAlpha - fAlpha * bAlpha;

        // Todo: qyl27: test and adjust.
        var aRed = (FastColor.ARGB32.red(foreground) * fAlpha) + FastColor.ARGB32.red(background) * (bAlpha);
        var aGreen = (FastColor.ARGB32.green(foreground) * fAlpha) + FastColor.ARGB32.green(background) * (bAlpha);
        var aBlue = (FastColor.ARGB32.blue(foreground) * fAlpha) + FastColor.ARGB32.blue(background) * (bAlpha);

        return of(aRed, aGreen, aBlue, clamp(a / 0.00390625));
    }

    private static int clamp(float aRed, float aGreen, float aBlue, float alpha) {
        return FastColor.ARGB32.color(clamp(alpha / 0.00390625), clamp(aRed / 0.00390625), clamp(aGreen / 0.00390625), clamp(aBlue / 0.00390625));
    }

    private static int clamp(int aRed, int aGreen, int aBlue, float alpha) {
        return clamp(clamp(alpha / 0.00390625), clamp(aRed), clamp(aGreen), clamp(aBlue));
    }

    private static int of(float aRed, float aGreen, float aBlue, float alpha) {
        // Todo: qyl27: test and adjust.
        return FastColor.ARGB32.color(clamp(alpha), clamp(aRed), clamp(aGreen), clamp(aBlue));
    }

    private static int clamp(double v) {
        return Math.min((int) v, 255);
    }

    public static int singleInvert(int c) {
        return INVERT_MAP.get(c);
    }

    private static int invert(int color) {
        var a = INVERT_MAP.get(FastColor.ARGB32.alpha(color));
        var r = INVERT_MAP.get(FastColor.ARGB32.red(color));
        var g = INVERT_MAP.get(FastColor.ARGB32.green(color));
        var b = INVERT_MAP.get(FastColor.ARGB32.blue(color));
        return FastColor.ARGB32.color(a, r, g, b);
    }

    public static int toNativeImage(int rgba) {
        var a = FastColor.ARGB32.alpha(rgba);
        var r = FastColor.ARGB32.red(rgba);
        var g = FastColor.ARGB32.green(rgba);
        var b = FastColor.ARGB32.blue(rgba);

        return NativeImage.combine(a, b, g, r);
    }

    public static Integer defaultMix(Triple<Integer, Integer, Integer> tripleFgBgAlpha) {
        var foreground = tripleFgBgAlpha.getLeft();
        var background = tripleFgBgAlpha.getMiddle();
        var alpha = tripleFgBgAlpha.getRight();

        var a = alphaFromIntAndInvert(alpha);

        return innerMix(foreground, background, 1 - a);
    }

    public static Integer mixWithBackgroundBlack(Triple<Integer, Integer, Integer> tripleFgBgAlpha) {
        var foreground = tripleFgBgAlpha.getLeft();
        var background = tripleFgBgAlpha.getMiddle();
        var alpha = tripleFgBgAlpha.getRight();

        var a = alphaFromIntAndInvert(alpha);

        // qyl27: we ignored the background.
        return innerMixWithBackgroundBlack(foreground, a);
    }

    public static float alphaFromIntAndInvert(int alpha) {
        return (float) (singleInvert(alpha) * 0.00390625);
    }

    public static float alphaFromInt(int alpha) {
        return (float) (alpha * 0.00390625);
    }

    private static int innerMixWithBackgroundBlack(int foreground, float alpha) {
        var r = FastColor.ARGB32.red(foreground);
        var g = FastColor.ARGB32.green(foreground);
        var b = FastColor.ARGB32.blue(foreground);

        var M = Math.max(r, Math.max(g, b));
        var m = Math.min(r, Math.min(g, b));

        var l = 0.5f * (M + m);

        var red = r * alpha;
        var green = g * alpha;
        var blue = b * alpha;

        return FastColor.ARGB32.color(255, clamp(red), clamp(green), clamp(blue));
    }
}
