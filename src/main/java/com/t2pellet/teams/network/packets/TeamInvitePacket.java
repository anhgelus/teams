package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.core.IHasTeam;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TeamInvitePacket extends ServerPacket {

    private static final String FROM_KEY = "fromId";
    private static final String TO_KEY = "toId";

    public TeamInvitePacket(UUID from, String to) {
        tag.putUuid(FROM_KEY, from);
        tag.putString(TO_KEY, to);
    }

    public TeamInvitePacket(MinecraftServer server, PacketByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        UUID from = tag.getUuid(FROM_KEY);
        UUID to = TeamsMod.getServer().getUserCache().findByName(tag.getString(TO_KEY)).orElseThrow().getId();

        ServerPlayerEntity fromPlayer = TeamsMod.getServer().getPlayerManager().getPlayer(from);
        ServerPlayerEntity toPlayer = TeamsMod.getServer().getPlayerManager().getPlayer(to);

        Team team = ((IHasTeam) fromPlayer).getTeam();
        if (team == null) {
            TeamsMod.LOGGER.error(fromPlayer.getName().getString() + " tried inviting " + toPlayer.getName().getString() + " but they are not in a team..");
        } else {
            try {
                TeamDB.INSTANCE.invitePlayerToTeam(toPlayer, team);
            } catch (Team.TeamException e) {
                TeamsMod.LOGGER.error(e.getMessage());
            }
        }
    }
}
