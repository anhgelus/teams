package com.t2pellet.teams.client.ui.toast;

import com.t2pellet.teams.client.TeamsKeys;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;

public abstract class RespondableTeamToast extends TeamToast {

    private boolean responded = false;

    public RespondableTeamToast(String team) {
        super(team);
    }

    public void respond() {
        responded = true;
    }

    @Override
    public String subTitle() {
        String rejectKey = TeamsKeys.REJECT.getLocalizedName();
        String acceptKey = TeamsKeys.ACCEPT.getLocalizedName();
        return I18n.translate("teams.toast.respond", rejectKey, acceptKey);
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        if (responded) {
            return Visibility.HIDE;
        }
        return super.draw(matrices, manager, startTime);
    }
}
