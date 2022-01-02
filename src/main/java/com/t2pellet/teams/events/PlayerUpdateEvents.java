package com.t2pellet.teams.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerUpdateEvents {

    public static final Event<PlayerHealthUpdate> PLAYER_HEALTH_UPDATE = EventFactory.createArrayBacked(PlayerHealthUpdate.class,
            listeners -> (player, health, hunger) -> {
                for (PlayerHealthUpdate listener : listeners) {
                    listener.onHealthUpdate(player, health, hunger);
                }
            });

    public static final Event<PlayerCopy> PLAYER_COPY = EventFactory.createArrayBacked(PlayerCopy.class,
            listeners -> (oldPlayer, newPlayer) -> {
                for (PlayerCopy listener : listeners) {
                    listener.onPlayerRespawn(oldPlayer, newPlayer);
                }
            });

    @FunctionalInterface
    public interface PlayerHealthUpdate {
        void onHealthUpdate(ServerPlayerEntity player, float health, int hunger);
    }

    @FunctionalInterface
    public interface PlayerCopy {
        void onPlayerRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer);
    }
}
