package com.t2pellet.teams.client.ui.toast;

import net.minecraft.client.resource.language.I18n;

import java.util.UUID;

public class ToastRequested extends RespondableTeamToast {

    public final UUID id;
    private final String name;

    public ToastRequested(String team, String name, UUID id) {
        super(team);
        this.name = name;
        this.id = id;
    }

    @Override
    public String title() {
        return I18n.translate("teams.toast.requested", name);
    }
}
