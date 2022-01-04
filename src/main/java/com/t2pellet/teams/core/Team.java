package com.t2pellet.teams.core;

import com.mojang.authlib.GameProfile;
import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamClearPacket;
import com.t2pellet.teams.network.packets.TeamDataPacket;
import com.t2pellet.teams.network.packets.TeamInitPacket;
import com.t2pellet.teams.network.packets.TeamPlayerDataPacket;
import com.t2pellet.teams.network.packets.toasts.TeamUpdatePacket;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.stream.Stream;

public class Team extends AbstractTeam {

    public final String name;
    private Set<UUID> players;
    private Map<UUID, ServerPlayerEntity> onlinePlayers;
    private net.minecraft.scoreboard.Team scoreboardTeam;

    Team(String name) {
        this.name = name;
        players = new HashSet<>();
        onlinePlayers = new HashMap<>();
        scoreboardTeam = TeamsMod.getScoreboard().getTeam(name);
        if (scoreboardTeam == null) {
            scoreboardTeam = TeamsMod.getScoreboard().addTeam(name);
        }
    }

    public UUID getOwner() {
        return players.stream().findFirst().orElseThrow();
    }

    public boolean playerHasPermissions(ServerPlayerEntity player) {
        return getOwner().equals(player.getUuid()) || player.hasPermissionLevel(2);
    }
    public Stream<ServerPlayerEntity> getOnlinePlayers() {
        return onlinePlayers.values().stream();
    }

    public Stream<UUID> getPlayers() {
        return players.stream();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean hasPlayer(ServerPlayerEntity player) {
        return hasPlayer(player.getUuid());
    }

    public boolean hasPlayer(UUID player) {
        return players.contains(player);
    }

    public void addPlayer(ServerPlayerEntity player) {
        addPlayer(player.getUuid());
    }

    private void addPlayer(UUID player) {
        players.add(player);
        String playerName = getNameFromUUID(player);
        var playerScoreboardTeam = TeamsMod.getScoreboard().getPlayerTeam(playerName);
        if (playerScoreboardTeam == null || !playerScoreboardTeam.isEqual(scoreboardTeam)) {
            TeamsMod.getScoreboard().addPlayerToTeam(playerName, scoreboardTeam);
        }
        var playerEntity = TeamsMod.getServer().getPlayerManager().getPlayer(player);
        if (playerEntity != null) {
            PacketHandler.INSTANCE.sendTo(new TeamUpdatePacket(name, playerName, TeamUpdatePacket.Action.JOINED, true), playerEntity);
            PacketHandler.INSTANCE.sendTo(new TeamUpdatePacket(name, playerName, TeamUpdatePacket.Action.JOINED, false), getOnlinePlayers().toArray(ServerPlayerEntity[]::new));
            playerOnline(playerEntity, true);
        }
    }

    public void removePlayer(ServerPlayerEntity player) {
        removePlayer(player.getUuid());
    }

    void removePlayer(UUID player) {
        players.remove(player);
        String playerName = getNameFromUUID(player);
        var playerScoreboardTeam = TeamsMod.getScoreboard().getPlayerTeam(playerName);
        if (playerScoreboardTeam != null && playerScoreboardTeam.isEqual(scoreboardTeam)) {
            TeamsMod.getScoreboard().removePlayerFromTeam(playerName, scoreboardTeam);
        }
        var playerEntity = TeamsMod.getServer().getPlayerManager().getPlayer(player);
        if (playerEntity != null) {
            playerOffline(playerEntity, true);
            PacketHandler.INSTANCE.sendTo(new TeamClearPacket(), playerEntity);
            PacketHandler.INSTANCE.sendTo(new TeamUpdatePacket(name, playerName, TeamUpdatePacket.Action.LEFT, true), playerEntity);
            PacketHandler.INSTANCE.sendTo(new TeamUpdatePacket(name, playerName, TeamUpdatePacket.Action.LEFT, false), getOnlinePlayers().toArray(ServerPlayerEntity[]::new));
            ((IHasTeam) playerEntity).setTeam(null);
        }
    }

    void playerOnline(ServerPlayerEntity player, boolean sendPackets) {
        onlinePlayers.put(player.getUuid(), player);
        ((IHasTeam) player).setTeam(this);
        // Packets
        if (sendPackets) {
            PacketHandler.INSTANCE.sendTo(new TeamInitPacket(name, playerHasPermissions(player)), player);
            if (onlinePlayers.size() == 1) {
                var players = TeamsMod.getServer().getPlayerManager().getPlayerList().toArray(ServerPlayerEntity[]::new);
                PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.ONLINE, name), players);
            }
            var players = getOnlinePlayers().toArray(ServerPlayerEntity[]::new);
            PacketHandler.INSTANCE.sendTo(new TeamPlayerDataPacket(player, TeamPlayerDataPacket.Type.ADD), players);
            for (var teammate : players) {
                PacketHandler.INSTANCE.sendTo(new TeamPlayerDataPacket(teammate, TeamPlayerDataPacket.Type.ADD), player);
            }
        }
    }

    void playerOffline(ServerPlayerEntity player, boolean sendPackets) {
        onlinePlayers.remove(player.getUuid());
        // Packets
        if (sendPackets) {
            if (isEmpty()) {
                var players = TeamsMod.getServer().getPlayerManager().getPlayerList().toArray(ServerPlayerEntity[]::new);
                PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.OFFLINE, name), players);
            }
            var players = getOnlinePlayers().toArray(ServerPlayerEntity[]::new);
            PacketHandler.INSTANCE.sendTo(new TeamPlayerDataPacket(player, TeamPlayerDataPacket.Type.REMOVE), players);
        }
    }

    void clear() {
        var playersCopy = new ArrayList<>(players);
        playersCopy.forEach(this::removePlayer);
    }

    private String getNameFromUUID(UUID id) {
        return TeamsMod.getServer().getUserCache().getByUuid(id).map(GameProfile::getName).orElseThrow();
    }

    static Team fromNBT(NbtCompound compound) {
        Team team = new Team.Builder(compound.getString("name"))
                .setColour(Formatting.byName(compound.getString("colour")))
                .setCollisionRule(CollisionRule.getRule(compound.getString("collision")))
                .setDeathMessageVisibilityRule(VisibilityRule.getRule(compound.getString("deathMessages")))
                .setNameTagVisibilityRule(VisibilityRule.getRule(compound.getString("nameTags")))
                .setFriendlyFireAllowed(compound.getBoolean("friendlyFire"))
                .setShowFriendlyInvisibles(compound.getBoolean("showInvisible"))
                .complete();

        NbtList list = compound.getList("players", NbtElement.STRING_TYPE);
        for (var elem : list) {
            team.addPlayer(UUID.fromString(elem.asString()));
        }

        return team;
    }

    NbtCompound toNBT() {
        NbtCompound compound = new NbtCompound();
        compound.putString("name", name);
        compound.putString("colour", scoreboardTeam.getColor().getName());
        compound.putString("collision", scoreboardTeam.getCollisionRule().name);
        compound.putString("deathMessages", scoreboardTeam.getDeathMessageVisibilityRule().name);
        compound.putString("nameTags", scoreboardTeam.getNameTagVisibilityRule().name);
        compound.putBoolean("friendlyFire", scoreboardTeam.isFriendlyFireAllowed());
        compound.putBoolean("showInvisible", scoreboardTeam.shouldShowFriendlyInvisibles());

        NbtList list = new NbtList();
        for (var player : players) {
            list.add(NbtString.of(player.toString()));
        }
        compound.put("players", list);

        return compound;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MutableText decorateName(Text name) {
        return scoreboardTeam.decorateName(name);
    }

    @Override
    public boolean shouldShowFriendlyInvisibles() {
        return scoreboardTeam.shouldShowFriendlyInvisibles();
    }

    public void setShowFriendlyInvisibles(boolean value) {
        scoreboardTeam.setShowFriendlyInvisibles(value);
    }

    @Override
    public boolean isFriendlyFireAllowed() {
        return scoreboardTeam.isFriendlyFireAllowed();
    }

    public void setFriendlyFireAllowed(boolean value) {
        scoreboardTeam.setFriendlyFireAllowed(value);
    }

    @Override
    public VisibilityRule getNameTagVisibilityRule() {
        return scoreboardTeam.getNameTagVisibilityRule();
    }

    public void setNameTagVisibilityRule(VisibilityRule value) {
        scoreboardTeam.setNameTagVisibilityRule(value);
    }

    @Override
    public Formatting getColor() {
        return scoreboardTeam.getColor();
    }

    public void setColour(Formatting colour) {
        scoreboardTeam.setColor(colour);
    }

    @Override
    public Collection<String> getPlayerList() {
        return scoreboardTeam.getPlayerList();
    }

    @Override
    public VisibilityRule getDeathMessageVisibilityRule() {
        return scoreboardTeam.getDeathMessageVisibilityRule();
    }

    public void setDeathMessageVisibilityRule(VisibilityRule value) {
        scoreboardTeam.setDeathMessageVisibilityRule(value);
    }

    @Override
    public CollisionRule getCollisionRule() {
        return scoreboardTeam.getCollisionRule();
    }

    public void setCollisionRule(CollisionRule value) {
        scoreboardTeam.setCollisionRule(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Team team && Objects.equals(team.getName(), this.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }


    public static class TeamException extends Exception {
        public TeamException(Text message) {
            super(message.getString());
        }
    }

    public static class Builder {

        private final String name;
        private boolean showFriendlyInvisibles = TeamsMod.getConfig().showInvisibleTeammates;
        private boolean friendlyFireAllowed = TeamsMod.getConfig().friendlyFireEnabled;
        private VisibilityRule nameTagVisibilityRule = TeamsMod.getConfig().nameTagVisibility;
        private Formatting colour = TeamsMod.getConfig().colour;
        private VisibilityRule deathMessageVisibilityRule = TeamsMod.getConfig().deathMessageVisibility;
        private CollisionRule collisionRule = TeamsMod.getConfig().collisionRule;

        public Builder(String name) {
            this.name = name;
        }

        public Builder setShowFriendlyInvisibles(boolean showFriendlyInvisibles) {
            this.showFriendlyInvisibles = showFriendlyInvisibles;
            return this;
        }

        public Builder setFriendlyFireAllowed(boolean friendlyFireAllowed) {
            this.friendlyFireAllowed = friendlyFireAllowed;
            return this;
        }

        public Builder setNameTagVisibilityRule(VisibilityRule nameTagVisibilityRule) {
            this.nameTagVisibilityRule = nameTagVisibilityRule;
            return this;
        }

        public Builder setColour(Formatting colour) {
            this.colour = colour;
            return this;
        }

        public Builder setDeathMessageVisibilityRule(VisibilityRule deathMessageVisibilityRule) {
            this.deathMessageVisibilityRule = deathMessageVisibilityRule;
            return this;
        }

        public Builder setCollisionRule(CollisionRule collisionRule) {
            this.collisionRule = collisionRule;
            return this;
        }

        public Team complete() {
            Team team = new Team(name);
            team.setShowFriendlyInvisibles(showFriendlyInvisibles);
            team.setFriendlyFireAllowed(friendlyFireAllowed);
            team.setNameTagVisibilityRule(nameTagVisibilityRule);
            team.setColour(colour);
            team.setDeathMessageVisibilityRule(deathMessageVisibilityRule);
            team.setCollisionRule(collisionRule);
            return team;
        }

    }
}
