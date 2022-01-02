package com.t2pellet.teams.client.ui.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import com.t2pellet.teams.client.TeamsKeys;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ToastInvite implements Toast {

    public final String team;
    private boolean responded = false;
    private boolean firstDraw = true;
    private long firstDrawTime;

    public ToastInvite(String team) {
        this.team = team;
    }

    public void respond() {
        this.responded = true;
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        if (firstDraw) {
            firstDrawTime = startTime;
            firstDraw = false;
        }
        if (responded) {
            return Visibility.HIDE;
        }
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 64, this.getWidth(), this.getHeight());
        manager.getClient().textRenderer.draw(matrices, I18n.translate("teams.toast.invite", team), 22, 7, Color.WHITE.getRGB());
        String rejectKey = TeamsKeys.REJECT.getLocalizedName();
        String acceptKey = TeamsKeys.ACCEPT.getLocalizedName();
        manager.getClient().textRenderer.draw(matrices, I18n.translate("teams.toast.respond", rejectKey, acceptKey), 22, 18, -16777216);

        return startTime - firstDrawTime < 15000L && team != null ? Visibility.SHOW : Visibility.HIDE;
    }

}
