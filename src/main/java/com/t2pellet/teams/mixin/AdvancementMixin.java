package com.t2pellet.teams.mixin;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.events.AdvancementEvents;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class AdvancementMixin {

    @Shadow private ServerPlayerEntity owner;

    @Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/PlayerAdvancementTracker;updateDisplay(Lnet/minecraft/advancement/Advancement;)V"))
    public void advancementCompleted(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> ci) {
        if (!TeamsMod.getServer().getOverworld().isClient) {
            AdvancementEvents.ADVANCEMENT_GRANTED.invoker().onPlayerAdvancement(owner, advancement);
        }
    }
}
