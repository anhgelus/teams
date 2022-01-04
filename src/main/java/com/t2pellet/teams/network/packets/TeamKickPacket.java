package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TeamKickPacket extends ServerPacket {

    private static final String TEAM_KEY = "teamName";
    private static final String SENDER_KEY = "senderId";
    private static final String KICKED_KEY = "kickedId";

    public TeamKickPacket(String team, UUID sender, UUID playerToKick) {
        tag.putString(TEAM_KEY, team);
        tag.putUuid(SENDER_KEY, sender);
        tag.putUuid(KICKED_KEY, playerToKick);
    }

    public TeamKickPacket(MinecraftServer server, PacketByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        Team team = TeamDB.INSTANCE.getTeam(tag.getString(TEAM_KEY));
        ServerPlayerEntity sender = TeamsMod.getServer().getPlayerManager().getPlayer(tag.getUuid(SENDER_KEY));
        if (sender != null && team.playerHasPermissions(sender)) {
            ServerPlayerEntity kicked = TeamsMod.getServer().getPlayerManager().getPlayer(tag.getUuid(KICKED_KEY));
            try {
                TeamDB.INSTANCE.removePlayerFromTeam(kicked);
            } catch (Team.TeamException ex) {
                TeamsMod.LOGGER.error(ex.getMessage());
            }
        } else {
            TeamsMod.LOGGER.error("Received packet to kick player, but the sender did not have permissions");
        }
    }
}
