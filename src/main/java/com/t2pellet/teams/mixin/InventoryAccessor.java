package com.t2pellet.teams.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface InventoryAccessor {

    @Accessor("x")
    int getX();
    @Accessor("y")
    int getY();
    @Accessor("backgroundWidth")
    int getBackgroundWidth();

}
