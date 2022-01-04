package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

public class TeamClearPacket extends ClientPacket {

    public TeamClearPacket() {
    }

    public TeamClearPacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        ClientTeam.INSTANCE.reset();
    }
}
