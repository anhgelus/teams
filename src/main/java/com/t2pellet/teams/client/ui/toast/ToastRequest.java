package com.t2pellet.teams.client.ui.toast;

public class ToastRequest extends TeamToast {

    public ToastRequest(String team) {
        super(team);
    }

    @Override
    public String title() {
        return "teams.toast.request";
    }

    @Override
    public String subTitle() {
        return "teams.toast.request.details";
    }
}
