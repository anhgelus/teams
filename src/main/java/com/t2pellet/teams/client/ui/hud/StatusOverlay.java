package com.t2pellet.teams.client.ui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.client.ClientTeam;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class StatusOverlay extends DrawableHelper {

    private static final Identifier ICON = new Identifier(TeamsMod.MODID, "textures/gui/icon.png");

    private final MinecraftClient client;
    private int offsetY = 0;

    public StatusOverlay() {
        this.client = MinecraftClient.getInstance();
    }

    public void render(MatrixStack matrices) {
        offsetY = 0;
        ClientTeam.INSTANCE.getTeammates().limit(4).forEach(teammate -> renderStatus(matrices, teammate));
    }

    private void renderStatus(MatrixStack matrices, ClientTeam.Teammate teammate) {
        // Dont render dead players
        if (teammate.getHealth() <= 0) return;
        
        int posX = (int) Math.round(client.getWindow().getScaledWidth() * 0.003);
        int posY = client.getWindow().getScaledHeight() / 4 - 5 + offsetY;

        // Health
        String health = String.valueOf(Math.round(teammate.getHealth()));
        RenderSystem.setShaderTexture(0, ICON);
        drawTexture(matrices, posX + 20, posY, 0, 0, 9, 9);
        drawTextWithShadow(matrices, client.textRenderer, new LiteralText(health), posX + 32, posY, Color.WHITE.getRGB());

        // Hunger
        String hunger = String.valueOf(teammate.getHunger());
        RenderSystem.setShaderTexture(0, ICON);
        drawTexture(matrices, posX + 46, posY, 9, 0, 9, 9);
        drawTextWithShadow(matrices, client.textRenderer, new LiteralText(hunger), posX + 58, posY, Color.WHITE.getRGB());

        // Draw skin
        RenderSystem.setShaderTexture(0, teammate.skin);
        matrices.push();
        matrices.scale(0.5F, 0.5F, 0.5F);
        drawTexture(matrices, posX + 4, client.getWindow().getScaledHeight() / 2 - 34 + 2 * offsetY, 32, 32, 32, 32);
        matrices.pop();

        // Draw name
        drawTextWithShadow(matrices, client.textRenderer, new LiteralText(teammate.name), (int) Math.round(client.getWindow().getScaledWidth() * 0.002) + 20, posY - 15, Color.WHITE.getRGB());

        // Update count & offset
        offsetY += 46;
    }

}
