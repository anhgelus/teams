package com.t2pellet.teams.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public abstract class ClientPacket extends Packet {

    protected ClientPacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(byteBuf);
        client.execute(this::execute);
    }

    protected ClientPacket() {
        super();
    }
}
