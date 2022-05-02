package games.moegirl.sinocraft.sinocalligraphy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.gui.GLSwitcher;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class TextureAtlas {

    public final ResourceLocation name;
    public final int width, height;
    private final Map<String, Tex> map = new HashMap<>();

    public TextureAtlas(ResourceLocation name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public TextureAtlas add(String name, float u, float v, int w, int h) {
        map.put(name, new Tex(u, v, w, h));
        return this;
    }

    public void blit(PoseStack stack, String name, int x, int y, int w, int h, GLSwitcher... configurations) {
        Tex tex = map.get(name);
        blit(stack, tex, x, y, w, h, configurations);
    }

    private void blit(PoseStack stack, Tex tex, int x, int y, int w, int h, GLSwitcher... configurations) {
        RenderSystem.setShaderTexture(0, this.name);
        GuiComponent.blit(stack, x, y, w, h, tex.u, tex.v, tex.w, tex.h, width, height);
        for (GLSwitcher configuration : configurations) {
            configuration.resume();
        }
    }

    record Tex(float u, float v, int w, int h) {}
}
