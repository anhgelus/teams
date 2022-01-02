package com.t2pellet.teams.core;

import net.minecraft.server.network.ServerPlayerEntity;

public interface IHasTeam {

    // Returns whether target is in team
    boolean hasTeam();

    // Returns target's team, or null if not in a team
    Team getTeam();

    void setTeam(Team team);

    boolean isTeammate(ServerPlayerEntity other);
}
