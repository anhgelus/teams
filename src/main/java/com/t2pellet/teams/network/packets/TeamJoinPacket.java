package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TeamJoinPacket extends ServerPacket {

    private static final String ID_KEY = "playerId";
    private static final String TEAM_KEY = "teamName";

    public TeamJoinPacket(UUID playerId, String team) {
        tag.putUuid(ID_KEY, playerId);
        tag.putString(TEAM_KEY, team);
    }

    public TeamJoinPacket(MinecraftServer server, PacketByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        UUID id = tag.getUuid(ID_KEY);
        ServerPlayerEntity player = TeamsMod.getServer().getPlayerManager().getPlayer(id);
        String teamName = tag.getString(TEAM_KEY);
        Team team = TeamDB.INSTANCE.getTeam(teamName);
        try {
            TeamDB.INSTANCE.addPlayerToTeam(player, team);
        } catch (Team.TeamException ex) {
            TeamsMod.LOGGER.error("Failed to join team: " + teamName);
            TeamsMod.LOGGER.error(ex.getMessage());
        }
    }
}
