package com.t2pellet.teams.client.ui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TexturedToggleWidget extends ButtonWidget {

    private final Identifier texture;
    private final int u;
    private final int v;
    private final int hoveredVOffset;
    private final int textureWidth;
    private final int textureHeight;
    private final ToggleCondition condition;

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, Identifier texture, ToggleCondition condition, PressAction pressAction) {
        this(x, y, width, height, u, v, height, texture, 256, 256, condition, pressAction);
    }

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, ToggleCondition condition, PressAction pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, 256, 256, condition, pressAction);
    }

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, ToggleCondition condition, PressAction pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, condition, pressAction, LiteralText.EMPTY);
    }

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, ToggleCondition condition, PressAction pressAction, Text text) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, condition, pressAction, EMPTY, text);
    }

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, ToggleCondition condition, PressAction pressAction, TooltipSupplier tooltipSupplier, Text text) {
        super(x, y, width, height, text, pressAction, tooltipSupplier);
        this.condition = condition;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.u = u;
        this.v = v;
        this.hoveredVOffset = hoveredVOffset;
        this.texture = texture;
    }

    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        int i = this.v;
        if (this.isHovered()) {
            i += this.hoveredVOffset;
        }
        int j = this.u;
        if (condition.isOn()) {
            j += this.width;
        }

        RenderSystem.enableDepthTest();
        drawTexture(matrices, this.x, this.y, (float)j, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
        if (this.hovered) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }
    }

    @FunctionalInterface
    public interface ToggleCondition {
        boolean isOn();
    }
}
