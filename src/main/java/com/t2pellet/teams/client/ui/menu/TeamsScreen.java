package com.t2pellet.teams.client.ui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.core.ClientTeam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class TeamsScreen extends Screen {

    public final Screen parent;
    protected final MinecraftClient client;
    protected int x;
    protected int y;
    protected boolean inTeam;

    public TeamsScreen(Screen parent, Text title) {
        super(title);
        client = TeamsModClient.client;
        this.parent = parent;
        inTeam = ClientTeam.INSTANCE.isInTeam();
    }

    @Override
    protected void init() {
        x = (width - getWidth()) / 2;
        y = (height - getHeight()) / 2;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getBackgroundTexture());
        matrices.push();
        matrices.scale(getBackgroundScale(), getBackgroundScale(), getBackgroundScale());
        drawTexture(matrices, (int) (x / getBackgroundScale()), (int) (y / getBackgroundScale()), 0, 0, (int) (getWidth() / getBackgroundScale()), (int) (getHeight() / getBackgroundScale()));
        matrices.pop();
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected abstract Identifier getBackgroundTexture();

    protected abstract float getBackgroundScale();

}
