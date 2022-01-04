package com.t2pellet.teams.client.ui.toast;

import net.minecraft.client.resource.language.I18n;

public class ToastInviteSent extends TeamToast {

    private String player;

    public ToastInviteSent(String team, String player) {
        super(team);
        this.player = player;
    }

    @Override
    public String title() {
        return I18n.translate("teams.toast.invitesent");
    }

    @Override
    public String subTitle() {
        return I18n.translate("teams.toast.invitesent.details", player);
    }
}
