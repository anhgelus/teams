package com.t2pellet.teams.client.ui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.client.ui.toast.ToastRequest;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamRequestPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

public class TeamEntry extends DrawableHelper implements Drawable, Element, Selectable {

    static final int WIDTH = 244;
    static final int HEIGHT = 24;
    private static final Identifier TEXTURE = new Identifier(TeamsMod.MODID, "textures/gui/screen_background.png");

    public final TexturedButtonWidget joinButton;
    private MinecraftClient client;
    private String team;
    private int x;
    private int y;

    public TeamEntry(String team, int x, int y) {
        this.client = MinecraftClient.getInstance();
        this.team = team;
        this.x = x;
        this.y = y;
        this.joinButton = new TexturedButtonWidget(x + WIDTH - 24, y + 8, 8, 8, 24, 190, TEXTURE, button -> {
            PacketHandler.INSTANCE.sendToServer(new TeamRequestPacket(team, client.player.getUuid()));
            client.getToastManager().add(new ToastRequest(team));
            client.setScreen(null);
        });
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Background
        renderBackground(matrices);
        // Name
        client.textRenderer.draw(matrices, team, x + 8, y + 12 - (int) (client.textRenderer.fontHeight / 2), Color.BLACK.getRGB());
        // Buttons
        joinButton.render(matrices, mouseX, mouseY, delta);
    }

    private void renderBackground(MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 166, WIDTH, HEIGHT);
    }

    @Override
    public SelectionType getType() {
        return SelectionType.FOCUSED;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO : implement this
    }
}
