package com.t2pellet.teams.client.core;

import com.t2pellet.teams.client.TeamsModClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.mixin.event.lifecycle.client.ClientWorldMixin;

@Environment(EnvType.CLIENT)
public class EventHandlers {

    public static ClientLoginConnectionEvents.Disconnect disconnect = (handler, client) -> {
        ClientTeam.INSTANCE.reset();
        ClientTeamDB.INSTANCE.clear();
    };

    public static ServerWorldEvents.Unload unload = (server, world) -> {
        if (TeamsModClient.client.isIntegratedServerRunning()) {
            ClientTeam.INSTANCE.reset();
            ClientTeamDB.INSTANCE.clear();
        }
    };
}
