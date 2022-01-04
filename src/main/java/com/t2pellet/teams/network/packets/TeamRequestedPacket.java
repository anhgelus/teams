package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.toast.ToastRequested;
import com.t2pellet.teams.network.ClientPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class TeamRequestedPacket extends ClientPacket {

    private static final String NAME_KEY = "playerName";
    private static final String ID_KEY = "playerId";

    public TeamRequestedPacket(String name, UUID id) {
        tag.putString(NAME_KEY, name);
        tag.putUuid(ID_KEY, id);
    }

    public TeamRequestedPacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    public void execute() {
        String name = tag.getString(NAME_KEY);
        UUID id = tag.getUuid(ID_KEY);
        TeamsModClient.client.getToastManager().add(new ToastRequested(ClientTeam.INSTANCE.getName(), name, id));
    }
}
