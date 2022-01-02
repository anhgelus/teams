package com.t2pellet.teams.core;

import com.t2pellet.teams.events.PlayerUpdateEvents;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamDataPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class EventHandlers {

    private EventHandlers() {
    }

    public static ServerPlayConnectionEvents.Join playerConnect = (handler, sender, server) -> {
        ServerPlayerEntity player = handler.getPlayer();
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            team.playerOnline(player);
        }
    };

    public static ServerPlayConnectionEvents.Disconnect playerDisconnect = (handler, server) ->  {
        ServerPlayerEntity player = handler.getPlayer();
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            team.playerOffline(player);
        }
    };

    public static PlayerUpdateEvents.PlayerHealthUpdate playerHealthUpdate = (player, health, hunger) -> {
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            ServerPlayerEntity[] players = team.getOnlinePlayers().filter(other -> !other.equals(player)).toArray(ServerPlayerEntity[]::new);
            PacketHandler.INSTANCE.sendTo(new TeamDataPacket(player, TeamDataPacket.Type.UPDATE), players);
        }
    };

    public static PlayerUpdateEvents.PlayerCopy playerCopy = (oldPlayer, newPlayer) -> {
        Team team = TeamDB.INSTANCE.getTeam(oldPlayer);
        if (team != null) {
            team.playerOffline(oldPlayer);
            team.playerOnline(newPlayer);
        }
    };

}
