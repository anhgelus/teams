package com.t2pellet.teams.client.ui.toast;

import net.minecraft.client.resource.language.I18n;

public class ToastLeave extends TeamToast {

    private String name;
    private boolean local;

    public ToastLeave(String team, String name, boolean local) {
        super(team);
        this.name = name;
        this.local = local;
    }

    @Override
    public String title() {
        return local ? I18n.translate("teams.toast.leave") : I18n.translate("teams.toast.left");
    }

    @Override
    public String subTitle() {
        return local ? I18n.translate("teams.toast.leave.details", team) : I18n.translate("teams.toast.left.details", name);
    }
}
