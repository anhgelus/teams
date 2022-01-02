package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.ui.toast.ToastInvite;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.network.ClientPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

public class TeamInvitePacket extends ClientPacket {

    private static final String TEAM_KEY = "teamName";

    public TeamInvitePacket(Team team) {
        tag.putString(TEAM_KEY, team.name);
    }

    public TeamInvitePacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    public void execute() {
        String team = tag.getString(TEAM_KEY);
        TeamsModClient.client.getToastManager().add(new ToastInvite(team));
    }
}
