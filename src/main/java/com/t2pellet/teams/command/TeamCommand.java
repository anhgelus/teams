package com.t2pellet.teams.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.t2pellet.teams.core.IHasTeam;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TeamCommand {

    private TeamCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("teams")
                .then(literal("create")
                        .then(argument("name", StringArgumentType.string())
                                .executes(TeamCommand::createTeam)))
                .then(literal("invite")
                        .then(argument("player", EntityArgumentType.player())
                                .executes(TeamCommand::invitePlayer)))
                .then(literal("leave")
                        .executes(TeamCommand::leaveTeam))
                .then(literal("kick")
                        .then(argument("player", EntityArgumentType.player())
                            .requires(source -> source.hasPermissionLevel(1))
                            .executes(TeamCommand::kickPlayer)))
                .then(literal("remove")
                        .then(argument("name", StringArgumentType.string())
                            .requires(source -> source.hasPermissionLevel(2))
                            .suggests(TeamSuggestions.TEAMS)
                            .executes(TeamCommand::removeTeam)))
                .then(literal("info")
                        .then(argument("name", StringArgumentType.string())
                                .suggests(TeamSuggestions.TEAMS)
                                .executes(TeamCommand::getTeamInfo)))
                .then(literal("list")
                        .executes(TeamCommand::listTeams)));

    }

    private static int createTeam(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String name = ctx.getArgument("name", String.class);
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        try {
            TeamDB.INSTANCE.addTeam(name, player);
        } catch (Team.TeamException e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(e.getMessage())).create();
        }
        ctx.getSource().sendFeedback(new TranslatableText("teams.success.create", name), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int invitePlayer(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        ServerPlayerEntity newPlayer = EntityArgumentType.getPlayer(ctx, "player");
        Team team = ((IHasTeam) player).getTeam();
        if (team == null) {
            throw new SimpleCommandExceptionType(new TranslatableText("teams.error.notinteam", player.getName().getString())).create();
        }
        try {
            TeamDB.INSTANCE.invitePlayerToTeam(newPlayer, team);
        } catch (Team.TeamException e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(e.getMessage())).create();
        }
        ctx.getSource().sendFeedback(new TranslatableText("teams.success.invite", newPlayer.getName().getString()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int leaveTeam(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        try {
            TeamDB.INSTANCE.removePlayerFromTeam(player);
        } catch (Team.TeamException e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(e.getMessage())).create();
        }
        ctx.getSource().sendFeedback(new TranslatableText("teams.success.leave"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int kickPlayer(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity otherPlayer = EntityArgumentType.getPlayer(ctx, "player");
        try {
            TeamDB.INSTANCE.removePlayerFromTeam(otherPlayer);
        } catch (Team.TeamException e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(e.getMessage())).create();
        }
        ctx.getSource().sendFeedback(new TranslatableText("teams.success.kick", otherPlayer.getName().getString()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int removeTeam(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String name = ctx.getArgument("name", String.class);
        Team team = TeamDB.INSTANCE.getTeam(name);
        if (team == null) {
            throw new SimpleCommandExceptionType(new TranslatableText("teams.error.invalidteam", name)).create();
        }
        TeamDB.INSTANCE.removeTeam(team);
        ctx.getSource().sendFeedback(new TranslatableText("teams.success.remove", name), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int listTeams(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(new TranslatableText("teams.success.list"), false);
        TeamDB.INSTANCE.getTeams().forEach(team -> {
            ctx.getSource().sendFeedback(new LiteralText(team.name), false);
        });
        return Command.SINGLE_SUCCESS;
    }

    private static int getTeamInfo(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String name = ctx.getArgument("name", String.class);
        Team team = TeamDB.INSTANCE.getTeam(name);
        if (team == null) {
            throw new SimpleCommandExceptionType(new TranslatableText("teams.error.invalidteam", name)).create();
        }
        ctx.getSource().sendFeedback(new TranslatableText("teams.success.info", name), false);
        team.getOnlinePlayers().forEach(player -> {
            ctx.getSource().sendFeedback(player.getName(), false);
        });
        return Command.SINGLE_SUCCESS;
    }

}
