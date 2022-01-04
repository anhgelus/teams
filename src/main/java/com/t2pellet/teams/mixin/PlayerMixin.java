package com.t2pellet.teams.mixin;

import com.mojang.authlib.GameProfile;
import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.core.IHasTeam;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.events.PlayerUpdateEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerMixin extends PlayerEntity implements IHasTeam {
	@Unique
	private Team team;

	@Shadow private float syncedHealth;
	@Shadow private int syncedFoodLevel;

	public PlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Override
	public boolean hasTeam() {
		return team != null;
	}

	@Override
	public Team getTeam() {
		return team;
	}

	@Override
	public void setTeam(Team team) {
		this.team = team;
	}

	@Override
	public boolean isTeammate(ServerPlayerEntity other) {
		return team.equals(((IHasTeam) other).getTeam());
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putBoolean(Ljava/lang/String;Z)V"), method = "writeCustomDataToNbt")
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
		if (team != null) {
			nbt.putString("playerTeam", team.getName());
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "readCustomDataFromNbt")
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
		if (team == null && nbt.contains("playerTeam")) {
			team = TeamDB.INSTANCE.getTeam(nbt.getString("playerTeam"));
			if (team == null || !team.hasPlayer(getUuid())) {
				team = null;
			}
		}
	}

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getHealth()F", ordinal = 1), method = "playerTick")
	private void playerTick(CallbackInfo info) {
		var player = (ServerPlayerEntity) ((Object) this);
		PlayerUpdateEvents.PLAYER_HEALTH_UPDATE.invoker().onHealthUpdate(player, player.getHealth(), player.getHungerManager().getFoodLevel());
	}

	@Inject(at = @At("TAIL"), method = "copyFrom")
	private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
		PlayerUpdateEvents.PLAYER_COPY.invoker().onPlayerRespawn(oldPlayer, (ServerPlayerEntity) ((Object) this));
	}

	@Override
	public boolean isSpectator() {
		return ((ServerPlayerEntity) (Object) this).interactionManager.getGameMode() == GameMode.SPECTATOR;
	}

	@Override
	public boolean isCreative() {
		return ((ServerPlayerEntity) (Object) this).interactionManager.getGameMode() == GameMode.CREATIVE;
	}
}
