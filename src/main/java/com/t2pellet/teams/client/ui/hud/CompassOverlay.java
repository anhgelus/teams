package com.t2pellet.teams.client.ui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.t2pellet.teams.client.ClientTeam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class CompassOverlay extends DrawableHelper {

    private static final int HUD_WIDTH = 182;
    private static final int HUD_HEIGHT = 5;

    private static final int MIN_DIST = 12;
    private static final int MAX_DIST = 128;
    private static final float MIN_SCALE = 0.2f;
    private static final float MAX_SCALE = 0.4f;
    private static final float MIN_ALPHA = 0.4f;

    private MinecraftClient client;

    public CompassOverlay() {
        this.client = MinecraftClient.getInstance();
    }

    public void render(MatrixStack matrices) {
        // Render bar
        if (ClientTeam.INSTANCE.getTeammates().findAny().isPresent()) {
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
            var x = (client.getWindow().getScaledWidth() - HUD_WIDTH) / 2;
            var y = (int) (client.getWindow().getScaledHeight() * 0.01) + HUD_HEIGHT;
            drawTexture(matrices, x, y, 0, 74, HUD_WIDTH, HUD_HEIGHT);
        }
        // Render heads
        ClientTeam.INSTANCE.getTeammates().forEach(teammate -> {
            PlayerEntity player = client.world.getPlayerByUuid(teammate.id);
            if (player != null) {
                double rotationHead = caculateRotationHead();
                float scaleFactor = calculateScaleFactor(player);
                double renderFactor = calculateRenderFactor(player, rotationHead);
                renderHUDHead(matrices, teammate.skin, scaleFactor, renderFactor);
            }
        });
    }

    private double caculateRotationHead() {
        double rotationHead = client.player.getHeadYaw() % 360;
        if (rotationHead > 180) {
            rotationHead = rotationHead - 360;
        } else if (rotationHead < -180) {
            rotationHead = 360 + rotationHead;
        }
        return rotationHead;
    }

    private float calculateScaleFactor(PlayerEntity player) {
        double diffPosX = player.getPos().x - client.player.getPos().x;
        double diffPosZ = player.getPos().z - client.player.getPos().z;
        double magnitude =  Math.sqrt(diffPosX * diffPosX + diffPosZ * diffPosZ);

        if (magnitude >= MAX_DIST) {
            return 1;
        } else if (magnitude <= MIN_DIST) {
            return 0;
        } else {
            return (float) ((magnitude - MIN_DIST) / (MAX_DIST - MIN_DIST));
        }
    }

    private double calculateRenderFactor(PlayerEntity player, double rotationHead) {
        double diffPosX = player.getPos().x - client.player.getPos().x;
        double diffPosZ = player.getPos().z - client.player.getPos().z;
        double magnitude = Math.sqrt(diffPosX * diffPosX + diffPosZ * diffPosZ);
        diffPosX /= magnitude;
        diffPosZ /= magnitude;
        double angle = Math.atan(diffPosZ / diffPosX) * 180 / Math.PI + 90;
        if (diffPosX >= 0) {
            angle -= 180;
        }
        double renderFactor = (angle - rotationHead) / 180;
        if (renderFactor > 1) {
            renderFactor = renderFactor - 2;
        }
        if (renderFactor < -1) {
            renderFactor = 2 + renderFactor;
        }
        return renderFactor;
    }

    private void renderHUDHead(MatrixStack matrices, Identifier skin, float scaleFactor, double renderFactor) {
        RenderSystem.setShaderTexture(0, skin);
        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();
        int x = (int) (scaledWidth / 2 - HUD_WIDTH / 4 + renderFactor * HUD_WIDTH / 2 + 41);
        int y = (int) ((scaledHeight * 0.01) + 12);
        float sizeFactor = scaleFactor * (MAX_SCALE - MIN_SCALE) + MIN_SCALE;
        float alphaFactor = (1 - scaleFactor) * (1 - MIN_ALPHA) + MIN_ALPHA;
        matrices.push();
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alphaFactor);
        matrices.scale(sizeFactor, sizeFactor, sizeFactor);
        if (1 - Math.abs(renderFactor) < Math.min(alphaFactor, 0.6f)) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) (1 - Math.abs(renderFactor)));
            drawTexture(matrices, (int) (x / sizeFactor), (int) (y / sizeFactor), 32, 32, 32, 32);
        } else {
            drawTexture(matrices, (int) (x / sizeFactor), (int) (y / sizeFactor), 32, 32, 32, 32);
        }
        RenderSystem.disableBlend();
        matrices.pop();
    }



}
