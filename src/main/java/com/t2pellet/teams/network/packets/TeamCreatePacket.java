package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TeamCreatePacket extends ServerPacket {

    private static final String TEAM_KEY = "teamName";
    private static final String PLAYER_KEY = "playerId";

    public TeamCreatePacket(String team, UUID player) {
        tag.putString(TEAM_KEY, team);
        tag.putUuid(PLAYER_KEY, player);
    }

    public TeamCreatePacket(MinecraftServer server, PacketByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        ServerPlayerEntity player = TeamsMod.getServer().getPlayerManager().getPlayer(tag.getUuid(PLAYER_KEY));
        try {
            TeamDB.INSTANCE.addTeam(tag.getString(TEAM_KEY), player);
        } catch (Team.TeamException e) {
            TeamsMod.LOGGER.error(e.getMessage());
        }
    }
}
