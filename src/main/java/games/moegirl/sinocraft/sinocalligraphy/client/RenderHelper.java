package games.moegirl.sinocraft.sinocalligraphy.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import games.moegirl.sinocraft.sinocore.utility.render.UVPairFloat;

import javax.annotation.Nullable;

public class RenderHelper {

    public static void add(VertexConsumer consumer, @Nullable PoseStack stack,
                           Vector3f pos, Vector4f color, @Nullable UVPairFloat uv) {
        if (stack == null) {
            consumer.vertex(pos.x(), pos.y(), pos.z())
                    .color(color.x(), color.y(), color.z(), color.w())
                    .endVertex();
        } else {
            consumer.vertex(stack.last().pose(), pos.x(), pos.y(), pos.z())
                    .color(color.x(), color.y(), color.z(), color.w());
            if (uv != null) {
                consumer.uv(uv.u, uv.v);
            } else {
                consumer.uv(0, 0);
            }
            consumer.overlayCoords(0, 240)
                    .normal(1, 0, 0);
            consumer.endVertex();
        }
    }

    public static void addSquare(VertexConsumer consumer, PoseStack stack,
                                 Vector3f pos1, @Nullable UVPairFloat uv1, Vector4f color1,
                                 Vector3f pos2, @Nullable UVPairFloat uv2, Vector4f color2,
                                 Vector3f pos3, @Nullable UVPairFloat uv3, Vector4f color3,
                                 Vector3f pos4, @Nullable UVPairFloat uv4, Vector4f color4) {
        add(consumer, stack, pos1, color1, uv1);
        add(consumer, stack, pos2, color2, uv2);
        add(consumer, stack, pos3, color3, uv3);
        add(consumer, stack, pos4, color4, uv4);
    }

    public static void addSquare(VertexConsumer consumer, PoseStack stack,
                                 Vector3f pos1, Vector3f pos2, Vector3f pos3, Vector3f pos4,
                                 @Nullable Vector4f uv, Vector4f color) {
        if (uv != null) {
            addSquare(consumer, stack,
                    pos1, new UVPairFloat(uv.x(), uv.z()), color,
                    pos2, new UVPairFloat(uv.y(), uv.z()), color,
                    pos3, new UVPairFloat(uv.x(), uv.w()), color,
                    pos4, new UVPairFloat(uv.y(), uv.w()), color);
        } else {
            addSquare(consumer, stack,
                    pos1, null, color,
                    pos2, null, color,
                    pos3, null, color,
                    pos4, null, color);
        }
    }

    public static Vector4f RGBAToGLColor(Vector4f rgba) {
        rgba.set(rgba.x() / 256, rgba.y() / 256, rgba.z() / 256, rgba.w() / 100);
        return rgba;
    }

    public static Vector4f GLColorToRGBA(Vector4f glColor) {
        glColor.set(glColor.x() * 256, glColor.y() * 256, glColor.z() * 256, glColor.w() * 100);
        return glColor;
    }

    public static Vector4f NumberToRGBA(int color) {
        float a = color >>> 24;
        float r = (color & 0xff0000) >> 16;
        float g = (color & 0xff00) >> 8;
        float b = color & 0xff;
        return new Vector4f(r, g, b, a);
    }

    public static Vector4f NumberToGLColor(int color) {
        Vector4f rgba = NumberToRGBA(color);
        return RGBAToGLColor(rgba);
    }
}
