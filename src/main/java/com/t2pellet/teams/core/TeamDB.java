package com.t2pellet.teams.core;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamDataPacket;
import com.t2pellet.teams.network.packets.toasts.TeamInvitedPacket;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TeamDB {

    public static final TeamDB INSTANCE = new TeamDB();
    private static final String TEAMS_KEY = "teams";

    private Map<String, Team> teams = new HashMap<>();

    private TeamDB() {
    }

    public Stream<Team> getTeams() {
        return teams.values().stream();
    }

    public void addTeam(Team team) throws Team.TeamException {
        if (teams.containsKey(team.getName())) {
            throw new Team.TeamException(MutableText.of(new LiteralTextContent("teams.error.duplicateteam")));
        }
        teams.put(team.getName(), team);
        ServerPlayerEntity[] players = TeamsMod.getServer().getPlayerManager().getPlayerList().toArray(ServerPlayerEntity[]::new);
        PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.ADD, team.name), players);
    }

    public Team addTeam(String name, @Nullable ServerPlayerEntity creator) throws Team.TeamException {
        if (creator != null && ((IHasTeam) creator).hasTeam()) {
            throw new Team.TeamException(MutableText.of(new LiteralTextContent("teams.error.alreadyinteam")));
        }
        Team team = new Team.Builder(name).complete();
        addTeam(team);
        if (creator != null) {
            team.addPlayer(creator);
        }
        ServerPlayerEntity[] players = TeamsMod.getServer().getPlayerManager().getPlayerList().toArray(ServerPlayerEntity[]::new);
        PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.ONLINE, team.name), players);
        return team;
    }

    public void removeTeam(Team team) {
        teams.remove(team.getName());
        TeamsMod.getScoreboard().removeTeam(TeamsMod.getScoreboard().getTeam(team.getName()));
        team.clear();
        ServerPlayerEntity[] players = TeamsMod.getServer().getPlayerManager().getPlayerList().toArray(ServerPlayerEntity[]::new);
        PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.REMOVE, team.name), players);
    }

    public boolean isEmpty() {
        return teams.isEmpty();
    }

    public boolean hasTeam(String team) {
        return teams.containsKey(team);
    }

    public Team getTeam(ServerPlayerEntity player) {
        return ((IHasTeam) player).getTeam();
    }

    public Team getTeam(String name) {
        return teams.get(name);
    }

    public void invitePlayerToTeam(ServerPlayerEntity player, Team team) throws Team.TeamException {
        if (((IHasTeam) player).hasTeam()) {
            throw new Team.TeamException(MutableText.of(new LiteralTextContent("teams.error.alreadyinteam")));
        }
        PacketHandler.INSTANCE.sendTo(new TeamInvitedPacket(team), player);
    }

    public void addPlayerToTeam(ServerPlayerEntity player, Team team) throws Team.TeamException {
        if (((IHasTeam) player).hasTeam()) {
            throw new Team.TeamException(MutableText.of(new LiteralTextContent("teams.error.alreadyinteam")));
        }
        team.addPlayer(player);
    }

    public void removePlayerFromTeam(ServerPlayerEntity player) throws Team.TeamException {
        Team playerTeam = ((IHasTeam) player).getTeam();
        if (playerTeam == null) {
            throw new Team.TeamException(MutableText.of(new LiteralTextContent("teams.error.notinteam")));
        }
        playerTeam.removePlayer(player);
        if (playerTeam.isEmpty()) {
            removeTeam(playerTeam);
        }
    }

    public void fromNBT(NbtCompound compound) {
        teams.clear();
        NbtList list = compound.getList(TEAMS_KEY, NbtElement.COMPOUND_TYPE);
        for (var tag : list) {
            try {
                addTeam(Team.fromNBT((NbtCompound) tag));
            } catch (Team.TeamException ex) {
                TeamsMod.LOGGER.error("Failed to load team from NBT" + ex.getMessage());
            }
        }
    }

    public NbtCompound toNBT() {
        NbtCompound compound = new NbtCompound();
        NbtList list = new NbtList();
        for (var team : teams.values()) {
            list.add(team.toNBT());
        }
        compound.put(TEAMS_KEY, list);
        return compound;
    }
}
