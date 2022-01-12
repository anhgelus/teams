package com.t2pellet.teams.mixin;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(PlayerAdvancementTracker.class)
public interface AdvancementAccessor {

    @Accessor("visibleAdvancements")
    Set<Advancement> getVisibleAdvancements();

}
