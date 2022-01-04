package com.t2pellet.teams.client;

import com.t2pellet.teams.client.core.EventHandlers;
import com.t2pellet.teams.client.ui.hud.CompassOverlay;
import com.t2pellet.teams.client.ui.hud.StatusOverlay;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.TeamPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class TeamsModClient implements ClientModInitializer {

    public static final MinecraftClient client = MinecraftClient.getInstance();
    public static final StatusOverlay status = new StatusOverlay();
    public static final CompassOverlay compass = new CompassOverlay();

    @Override
    public void onInitializeClient() {
        // Register HUDs
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            status.render(matrixStack);
            compass.render(matrixStack);
        });
        // Register Keybinds
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (var key : TeamsKeys.KEYS) {
                if (key.keyBinding.wasPressed()) {
                    key.onPress.execute(client);
                }
            }
        });
        // Register events
        ClientLoginConnectionEvents.DISCONNECT.register(EventHandlers.disconnect);
        ServerWorldEvents.UNLOAD.register(EventHandlers.unload);
    }

}
