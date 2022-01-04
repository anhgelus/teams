package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TeamLeavePacket extends ServerPacket {

    private static final String PLAYER_KEY = "playerId";

    public TeamLeavePacket(UUID player) {
        tag.putUuid(PLAYER_KEY, player);
    }

    public TeamLeavePacket(MinecraftServer server, PacketByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        ServerPlayerEntity player = TeamsMod.getServer().getPlayerManager().getPlayer(tag.getUuid(PLAYER_KEY));
        try {
            TeamDB.INSTANCE.removePlayerFromTeam(player);
        } catch (Team.TeamException ex) {
            TeamsMod.LOGGER.error(ex.getMessage());
        }
    }
}
