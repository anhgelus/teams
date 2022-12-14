package com.t2pellet.teams.network.packets.toasts;

import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.ui.toast.ToastJoin;
import com.t2pellet.teams.client.ui.toast.ToastLeave;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

public class TeamUpdatePacket extends ClientPacket {

    public enum Action {
        JOINED,
        LEFT
    }

    private static final String TEAM_KEY = "teamName";
    private static final String PLAYER_KEY = "playerName";
    private static final String ACTION_KEY = "action";
    private static final String LOCAL_KEY = "local";

    public TeamUpdatePacket(String team, String player, Action action, boolean isLocal) {
        tag.putString(TEAM_KEY, team);
        tag.putString(PLAYER_KEY, player);
        tag.putString(ACTION_KEY, action.name());
        tag.putBoolean(LOCAL_KEY, isLocal);
    }

    public TeamUpdatePacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        String team = tag.getString(TEAM_KEY);
        String player = tag.getString(PLAYER_KEY);
        Action action = Action.valueOf(tag.getString(ACTION_KEY));
        boolean isLocal = tag.getBoolean(LOCAL_KEY);

        switch (action) {
            case JOINED -> TeamsModClient.client.getToastManager().add(new ToastJoin(team, player, isLocal));
            case LEFT -> TeamsModClient.client.getToastManager().add(new ToastLeave(team, player, isLocal));
        }
    }
}
