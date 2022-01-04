package com.t2pellet.teams.network.packets.toasts;

import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.ui.toast.ToastInvited;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

public class TeamInvitedPacket extends ClientPacket {

    private static final String TEAM_KEY = "teamName";

    public TeamInvitedPacket(Team team) {
        tag.putString(TEAM_KEY, team.getName());
    }

    public TeamInvitedPacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        String team = tag.getString(TEAM_KEY);
        TeamsModClient.client.getToastManager().add(new ToastInvited(team));
    }
}
