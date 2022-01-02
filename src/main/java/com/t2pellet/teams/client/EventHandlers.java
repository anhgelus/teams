package com.t2pellet.teams.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
@Environment(EnvType.CLIENT)

public class EventHandlers {

    public static ClientLoginConnectionEvents.Disconnect disconnect = (handler, client) -> {
        ClientTeam.INSTANCE.clear();
    };
}
