package games.moegirl.sinocraft.sinocalligraphy.gui.components;

import com.google.common.base.Verify;
import com.mojang.blaze3d.vertex.PoseStack;
import games.moegirl.sinocraft.sinocalligraphy.gui.GLSwitcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.awt.*;
import java.time.Duration;

public class AnimatedText extends AbstractWidget {

    private long beginTime;
    private double duration;
    private int repeat;
    private Color color = Color.BLACK;

    private Font font;

    public AnimatedText(int pX, int pY, int pWidth, int pHeight, Font font) {
        super(pX, pY, pWidth, pHeight, TextComponent.EMPTY);
        this.font = font;
    }

    public AnimatedText(int pWidth, int pHeight) {
        this(0, 0, pWidth, pHeight, Minecraft.getInstance().font);
    }

    public void begin(Duration duration, int repeat, Color color, Component message) {
        Verify.verify(duration.toNanos() > 0, "duration must not 0, your is " + duration);
        Verify.verify(message != TextComponent.EMPTY, "message must not empty");

        setMessage(message);
        this.beginTime = System.nanoTime();
        this.duration = duration.toNanos();
        this.repeat = repeat;
        this.color = color;
    }

    public void begin(Duration duration, int repeat, Color color, String message) {
        begin(duration, repeat, color, new TranslatableComponent(message));
    }

    public void begin(Duration duration, int repeat, Color color, String message, Object pArgs) {
        begin(duration, repeat, color, new TranslatableComponent(message, pArgs));
    }

    public void stop() {
        setMessage(TextComponent.EMPTY);
        beginTime = 0;
        duration = 0;
        repeat = 0;
    }

    public void stopSmooth() {
        repeat = 0;
    }

    public AnimatedText resize(int x, int y, Font font) {
        this.x = x;
        this.y = y;
        this.font = font;
        return this;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        Component message = getMessage();
        if (message != TextComponent.EMPTY && duration > 0) {
            long delta = System.nanoTime() - beginTime;
            if (delta >= duration) {
                if (repeat != 0) {
                    if (repeat > 0) {
                        repeat--;
                    }
                    beginTime = System.nanoTime();
                } else {
                    stop();
                }
            } else {
                double p = delta / duration;
                if (p > 0.5) {
                    p = (1 - p) * 2;
                } else {
                    p *= 2;
                }
                GLSwitcher switcher = GLSwitcher.blend().enable();
                renderMessage(pPoseStack, message, p);
                switcher.resume();
            }
        }
    }

    private void renderMessage(PoseStack poseStack, Component message, double alpha) {
        Color c = new Color(color.getRed(), color.getGreen(), color.getGreen(), (int) (alpha * 255));
        int textWidth = font.width(message);
        font.draw(poseStack, message, x - textWidth / 2f, y, c.getRGB());
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, "Animated text");
    }
}
