package com.t2pellet.teams.client.ui.toast;

import net.minecraft.client.resource.language.I18n;

public class ToastRequest extends TeamToast {

    public ToastRequest(String team) {
        super(team);
    }

    @Override
    public String title() {
        return I18n.translate("teams.toast.request");
    }

    @Override
    public String subTitle() {
        return I18n.translate("teams.toast.request.details", team);
    }
}
