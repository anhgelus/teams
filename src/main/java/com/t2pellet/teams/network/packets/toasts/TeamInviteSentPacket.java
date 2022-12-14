package com.t2pellet.teams.network.packets.toasts;

import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.ui.toast.ToastInviteSent;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

public class TeamInviteSentPacket extends ClientPacket {

    private static final String TEAM_KEY = "teamName";
    private static final String NAME_KEY = "playerName";

    public TeamInviteSentPacket(String team, String player) {
        tag.putString(TEAM_KEY, team);
        tag.putString(NAME_KEY, player);
    }

    public TeamInviteSentPacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        String team = tag.getString(TEAM_KEY);
        String name = tag.getString(NAME_KEY);
        TeamsModClient.client.getToastManager().add(new ToastInviteSent(team, name));
    }
}
