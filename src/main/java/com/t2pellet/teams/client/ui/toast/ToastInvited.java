package com.t2pellet.teams.client.ui.toast;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;

@Environment(EnvType.CLIENT)
public class ToastInvited extends RespondableTeamToast {

    public ToastInvited(String team) {
        super(team);
    }

    @Override
    public String title() {
        return I18n.translate("teams.toast.invite", team);
    }

}
