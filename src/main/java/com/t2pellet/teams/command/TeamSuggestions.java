package com.t2pellet.teams.command;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.util.stream.Stream;

public class TeamSuggestions {

    private TeamSuggestions() {
    }

    static final SuggestionProvider<ServerCommandSource> TEAMS = SuggestionProviders.register(new Identifier("teams"), (context, builder) -> {
        Stream<Team> teams = TeamDB.INSTANCE.getTeams();
        teams.forEach(team -> {
            builder.suggest(team.getName());
        });
        return builder.buildFuture();
    });

}
