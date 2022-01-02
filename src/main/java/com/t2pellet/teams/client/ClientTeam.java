package com.t2pellet.teams.client;

import com.t2pellet.teams.TeamsMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class ClientTeam {

    public static class Teammate {
        public final UUID id;
        public final String name;
        public final Identifier skin;
        private float health;
        private int hunger;

        public Teammate(UUID id, String name, Identifier skin, float health, int hunger) {
            this.id = id;
            this.name = name;
            this.skin = skin;
            this.health = health;
            this.hunger = hunger;
        }

        public float getHealth() {
            return health;
        }

        public int getHunger() {
            return hunger;
        }
    }

    public static final ClientTeam INSTANCE = new ClientTeam();

    private MinecraftClient client = MinecraftClient.getInstance();
    private Map<UUID, Teammate> teammates = new HashMap<>();

    private ClientTeam() {
    }

    public Stream<Teammate> getTeammates() {
        return teammates.values().stream();
    }

    public boolean hasPlayer(UUID player) {
        return teammates.containsKey(player);
    }

    public void addPlayer(UUID player, Teammate teammate) {
        if (!client.player.getUuid().equals(player)) {
            teammates.put(player, teammate);
        }
    }

    public void updatePlayer(UUID player, float health, int hunger) {
        var teammate = teammates.get(player);
        if (teammate != null) {
            teammate.health = health;
            teammate.hunger = hunger;
        } else {
            TeamsMod.LOGGER.warn("Tried updating player with UUID " + player + "but they are not in this clients team");
        }
    }

    public void removePlayer(UUID player) {
        teammates.remove(player);
    }

    public void clear() {
        teammates.clear();
    }

}
