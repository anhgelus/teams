package com.t2pellet.teams.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

public abstract class ClientPacket extends Packet {

    public ClientPacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(byteBuf);
        client.execute(this::execute);
    }

    protected ClientPacket() {
        super();
    }
}
