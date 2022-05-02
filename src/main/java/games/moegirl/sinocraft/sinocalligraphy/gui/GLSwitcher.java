package games.moegirl.sinocraft.sinocalligraphy.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public abstract class GLSwitcher {

    private final boolean isEnabled;

    public GLSwitcher(int code) {
        this.isEnabled = GL11.glIsEnabled(code);
    }

    protected abstract void _enable();

    protected abstract void _disable();

    public GLSwitcher set(boolean enable) {
        if (enable) {
            _enable();
        } else {
            _disable();
        }
        return this;
    }

    public GLSwitcher set(State state) {
        switch (state) {
            case ENABLE -> set(true);
            case DISABLE -> set(false);
        }
        return this;
    }

    public GLSwitcher enable() {
        _enable();
        return this;
    }

    public GLSwitcher disable() {
        _disable();
        return this;
    }

    public void resume() {
        if (isEnabled) {
            _enable();
        } else {
            _disable();
        }
    }

    public static GLSwitcher blend() {
        return new GLSwitcher(GL11.GL_DEPTH_TEST) {
            @Override
            protected void _enable() {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
            }

            @Override
            protected void _disable() {
                RenderSystem.disableBlend();
            }
        };
    }

    public static GLSwitcher depth() {
        return new GLSwitcher(GL11.GL_DEPTH_TEST) {
            @Override
            protected void _enable() {
                RenderSystem.enableDepthTest();
            }

            @Override
            protected void _disable() {
                RenderSystem.disableDepthTest();
            }
        };
    }

    public enum State {
        ENABLE, DISABLE, IGNORE
    }
}
