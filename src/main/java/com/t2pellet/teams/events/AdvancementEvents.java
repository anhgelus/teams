package com.t2pellet.teams.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.server.network.ServerPlayerEntity;

public class AdvancementEvents {

    public static final Event<PlayerAdvancement> ADVANCEMENT_GRANTED = EventFactory.createArrayBacked(PlayerAdvancement.class,
            listeners -> (player, advancement) -> {
                for (PlayerAdvancement listener : listeners) {
                    listener.onPlayerAdvancement(player, advancement);
                }
            });

    @FunctionalInterface
    public interface PlayerAdvancement {
        void onPlayerAdvancement(ServerPlayerEntity player, Advancement advancement);
    }

}
