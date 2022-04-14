package games.moegirl.sinocraft.sinocalligraphy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import games.moegirl.sinocraft.sinocore.utility.render.UVPairFloat;
import net.minecraft.client.renderer.GameRenderer;

import javax.annotation.Nullable;

public class RenderHelper {
    public static void add(VertexConsumer consumer, @Nullable PoseStack stack,
                           Vector3f pos, Vector4f color, @Nullable UVPairFloat tex) {
        if (stack == null) {
            consumer.vertex(pos.x(), pos.y(), pos.z())
                    .color(color.x(), color.y(), color.z(), color.w())
                    .endVertex();
        } else {
            consumer.vertex(stack.last().pose(), pos.x(), pos.y(), pos.z())
                    .color(color.x(), color.y(), color.z(), color.w());
            if (tex != null) {
                consumer.uv(tex.u, tex.v);
            } else {
                consumer.uv(0, 0);
            }

            consumer.uv2(0, 240)
                    .normal(1, 1, 1);

            consumer.endVertex();
        }
    }

    public static void addSquare(VertexConsumer consumer, PoseStack stack,
                                 Vector3f pos1, Vector4f color1, @Nullable UVPairFloat tex1,
                                 Vector3f pos2, Vector4f color2, @Nullable UVPairFloat tex2,
                                 Vector3f pos3, Vector4f color3, @Nullable UVPairFloat tex3,
                                 Vector3f pos4, Vector4f color4, @Nullable UVPairFloat tex4) {
        add(consumer, stack, pos1, color1, tex1);
        add(consumer, stack, pos2, color2, tex2);
        add(consumer, stack, pos3, color3, tex3);
        add(consumer, stack, pos4, color4, tex4);
    }

    public static void addSquare(VertexConsumer consumer, PoseStack stack,
                                 Vector3f pos1, Vector3f pos2, Vector3f pos3, Vector3f pos4
                                , Vector4f color, @Nullable Vector4f tex) {
        if (tex != null) {
            addSquare(consumer, stack,
                    pos1, color, new UVPairFloat(tex.x(), tex.z()),
                    pos2, color, new UVPairFloat(tex.y(), tex.z()),
                    pos3, color, new UVPairFloat(tex.x(), tex.w()),
                    pos4, color, new UVPairFloat(tex.y(), tex.w()));
        } else {
            addSquare(consumer, stack,
                    pos1, color, null,
                    pos2, color, null,
                    pos3, color, null,
                    pos4, color, null);
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
