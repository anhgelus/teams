package com.t2pellet.teams.client.ui.toast;

import net.minecraft.client.resource.language.I18n;

public class ToastJoin extends TeamToast {

    private String name;
    private boolean local;

    public ToastJoin(String team, String name, boolean local) {
        super(team);
        this.name = name;
        this.local = local;
    }

    @Override
    public String title() {
        return local ? I18n.translate("teams.toast.join") : I18n.translate("teams.toast.joined");
    }

    @Override
    public String subTitle() {
        return local ? I18n.translate("teams.toast.join.details", team) : I18n.translate("teams.toast.joined.details", name);
    }
}
