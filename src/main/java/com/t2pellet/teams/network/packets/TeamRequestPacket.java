package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TeamRequestPacket extends ServerPacket {

    private static final String TEAM_KEY = "teamName";
    private static final String PLAYER_KEY = "playerId";

    public TeamRequestPacket(String team, UUID player) {
        tag.putString(TEAM_KEY, team);
        tag.putUuid(PLAYER_KEY, player);
    }

    public TeamRequestPacket(MinecraftServer server, PacketByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        String name = tag.getString(TEAM_KEY);
        Team team = TeamDB.INSTANCE.getTeam(name);
        if (team == null) {
            throw new IllegalArgumentException("Got request to join team " + name + ", but that team doesn't exist");
        } else {
            // Get first online player in list of seniority
            var playerManager = TeamsMod.getServer().getPlayerManager();
            ServerPlayerEntity seniorPlayer = team.getPlayers()
                    .filter(p -> playerManager.getPlayer(p) != null)
                    .map(playerManager::getPlayer)
                    .findFirst().orElseThrow();
            PacketHandler.INSTANCE.sendTo(new TeamRequestedPacket(name, tag.getUuid(PLAYER_KEY)), seniorPlayer);
        }
    }
}
