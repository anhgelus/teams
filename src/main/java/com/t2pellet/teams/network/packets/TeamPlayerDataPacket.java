package com.t2pellet.teams.network.packets;

import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FilenameUtils;

import java.util.Base64;
import java.util.UUID;

public class TeamPlayerDataPacket extends ClientPacket {

    private static final String ID_KEY = "playerUuid";
    private static final String NAME_KEY = "playerName";
    private static final String SKIN_KEY = "playerSkin";
    private static final String HEALTH_KEY = "playerHealth";
    private static final String HUNGER_KEY = "playerHunger";
    private static final String TYPE_KEY = "actionType";

    public enum Type {
        ADD,
        UPDATE,
        REMOVE,
    }

    public TeamPlayerDataPacket(ServerPlayerEntity player, Type type) {
        var health = player.getHealth();
        var hunger = player.getHungerManager().getFoodLevel();
        tag.putUuid(ID_KEY, player.getUuid());
        tag.putString(TYPE_KEY, type.toString());
        switch (type) {
            case ADD -> {
                tag.putString(NAME_KEY, player.getName().getString());
                var properties = player.getGameProfile().getProperties();
                String skin = "";
                if (properties.containsKey("textures")) {
                    skin = properties.get("textures").iterator().next().getValue();
                }
                tag.putString(SKIN_KEY, skin);
                tag.putFloat(HEALTH_KEY, health);
                tag.putInt(HUNGER_KEY, hunger);
            }
            case UPDATE -> {
                tag.putFloat(HEALTH_KEY, health);
                tag.putInt(HUNGER_KEY, hunger);
            }
        }
    }

    public TeamPlayerDataPacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        UUID uuid = tag.getUuid(ID_KEY);
        switch (Type.valueOf(tag.getString(TYPE_KEY))) {
            case ADD -> {
                if (ClientTeam.INSTANCE.hasPlayer(uuid)) return;

                String name = tag.getString(NAME_KEY);
                float health = tag.getFloat(HEALTH_KEY);
                int hunger = tag.getInt(HUNGER_KEY);

                // Get skin
                Identifier identifier;
                String skin = tag.getString(SKIN_KEY);
                if (skin.isEmpty()) {
                    identifier = DefaultSkinHelper.getTexture(uuid);
                } else {
                    byte[] decodedBytes = Base64.getDecoder().decode(skin);
                    String decodedString = new String(decodedBytes);
                    JsonObject decodedJson = JsonParser.parseString(decodedString).getAsJsonObject();
                    String skinURL = decodedJson.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
                    String string = Hashing.sha1().hashUnencodedChars(FilenameUtils.getBaseName(skinURL)).toString();
                    identifier = new Identifier("skins/" + string);
                }
                ClientTeam.INSTANCE.addPlayer(uuid, name, identifier, health, hunger);
            }
            case UPDATE -> {
                float health = tag.getFloat(HEALTH_KEY);
                int hunger = tag.getInt(HUNGER_KEY);
                ClientTeam.INSTANCE.updatePlayer(uuid, health, hunger);
            }
            case REMOVE -> {
                ClientTeam.INSTANCE.removePlayer(uuid);
            }
        }
    }
}
