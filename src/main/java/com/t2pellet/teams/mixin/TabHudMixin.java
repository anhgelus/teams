package com.t2pellet.teams.mixin;

import com.t2pellet.teams.client.TeamsModClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(PlayerListHud.class)
public class TabHudMixin {

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 9)
    private int onRenderTabList(int p) {
        if (TeamsModClient.compass.isShowing()) {
            float scaledHeight = TeamsModClient.client.getWindow().getScaledHeight();
            return (int) (scaledHeight * 0.01) + 12 + 16;
        }
        return p;
    }

}
