package com.t2pellet.teams.client.ui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamKickPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
public class TeammateEntry extends DrawableHelper implements Drawable, Element, Selectable {

    static final int WIDTH = 244;
    static final int HEIGHT = 24;
    private static final Identifier TEXTURE = new Identifier(TeamsMod.MODID, "textures/gui/screen_background.png");

    private TexturedButtonWidget kickButton;
    private TexturedToggleWidget favButton;
    private MinecraftClient client;
    private TeamsMainScreen parent;
    private ClientTeam.Teammate teammate;
    private int x;
    private int y;

    public TeammateEntry(TeamsMainScreen parent, ClientTeam.Teammate teammate, int x, int y, boolean local) {
        this.client = MinecraftClient.getInstance();
        this.parent = parent;
        this.teammate = teammate;
        this.x = x;
        this.y = y;
        if (!local) {
            this.favButton = new TexturedToggleWidget(x + WIDTH - 12, y + 8, 8, 8, 0, 190, TEXTURE, () -> {
                return ClientTeam.INSTANCE.isFavourite(teammate);
            }, button -> {
                if (ClientTeam.INSTANCE.isFavourite(teammate)) {
                    ClientTeam.INSTANCE.removeFavourite(teammate);
                } else {
                    ClientTeam.INSTANCE.addFavourite(teammate);
                }
            });
        }
        if (ClientTeam.INSTANCE.hasPermissions()) {
            this.kickButton = new TexturedButtonWidget(x + WIDTH - 24, y + 8, 8, 8, 16, 190, TEXTURE, button -> {
                PacketHandler.INSTANCE.sendToServer(new TeamKickPacket(ClientTeam.INSTANCE.getName(), client.player.getUuid(), teammate.id));
                ClientTeam.INSTANCE.removePlayer(teammate.id);
            });
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Background
        renderBackground(matrices);
        // Head
        float scale = 0.5F;
        matrices.push();
        matrices.scale(scale, scale, scale);
        RenderSystem.setShaderTexture(0, teammate.skin);
        drawTexture(matrices, (int) ((x + 4) / scale), (int) ((y + 4) / scale), 32, 32, 32, 32);
        matrices.pop();
        // Nameplate
        client.textRenderer.draw(matrices, teammate.name, x + 24, y + 12 - (int) (client.textRenderer.fontHeight / 2), Color.BLACK.getRGB());
        // Buttons
        if (favButton != null) {
            favButton.render(matrices, mouseX, mouseY, delta);
        }
        if (kickButton != null) {
            kickButton.render(matrices, mouseX, mouseY, delta);
        }
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

    public TexturedButtonWidget getKickButton() {
        return kickButton;
    }

    public TexturedToggleWidget getFavButton() {
        return favButton;
    }
}
