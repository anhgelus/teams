package com.t2pellet.teams.client;

import com.t2pellet.teams.client.ui.toast.ToastInvite;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamJoinPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class TeamsKeys {

    public static class TeamsKey {
        @FunctionalInterface
        public interface OnPress {
            void execute(MinecraftClient client);
        }

        private TeamsKey(String keyName, int keyBind, OnPress action) {
            keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    keyName,
                    InputUtil.Type.KEYSYM,
                    keyBind,
                    "category.teams"
            ));
            onPress = action;
        }

        public String getLocalizedName() {
            return keyBinding.getBoundKeyLocalizedText().asString();
        }

        final KeyBinding keyBinding;
        final OnPress onPress;
    }

    public static final TeamsKey ACCEPT = new TeamsKey("key.teams.accept", GLFW.GLFW_KEY_RIGHT_BRACKET, client -> {
        var toastManager = client.getToastManager();
        ToastInvite toast = toastManager.getToast(ToastInvite.class, Toast.TYPE);
        if (toast != null) {
            toast.respond();
            PacketHandler.INSTANCE.sendToServer(new TeamJoinPacket(client.player.getUuid(), toast.team));
        }
    });

    public static final TeamsKey REJECT = new TeamsKey("key.teams.reject", GLFW.GLFW_KEY_LEFT_BRACKET, client -> {
        var toastManager = client.getToastManager();
        ToastInvite toast = toastManager.getToast(ToastInvite.class, Toast.TYPE);
        if (toast != null) {
            toast.respond();
        }
    });

    static final TeamsKey[] KEYS = {
            ACCEPT,
            REJECT
    };

}
