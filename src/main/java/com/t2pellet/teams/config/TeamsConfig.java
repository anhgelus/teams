package com.t2pellet.teams.config;

import com.t2pellet.teams.TeamsMod;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.Formatting;

@Config(name = TeamsMod.MODID)
public class TeamsConfig implements ConfigData {

    @ConfigEntry.Category("Team Defaults")
    public boolean showInvisibleTeammates = true;
    @ConfigEntry.Category("Team Defaults")
    public boolean friendlyFireEnabled = false;
    @ConfigEntry.Category("Team Defaults")
    public AbstractTeam.VisibilityRule nameTagVisibility = AbstractTeam.VisibilityRule.ALWAYS;
    @ConfigEntry.Category("Team Defaults")
    public Formatting colour = Formatting.BOLD;
    @ConfigEntry.Category("Team Defaults")
    public AbstractTeam.VisibilityRule deathMessageVisibility = AbstractTeam.VisibilityRule.ALWAYS;
    @ConfigEntry.Category("Team Defaults")
    @Comment("Note that 'push own team' and 'push other teams' are swapped.")
    public AbstractTeam.CollisionRule collisionRule = AbstractTeam.CollisionRule.PUSH_OWN_TEAM;

    @ConfigEntry.Category("Visual")
    public boolean enableCompassHUD = true;
    @ConfigEntry.Category("Visual")
    public boolean enableStatusHUD = true;
    @ConfigEntry.Category("Visual")
    @Comment("How long teams toast notifications should last")
    public int toastDuration = 5;

}
