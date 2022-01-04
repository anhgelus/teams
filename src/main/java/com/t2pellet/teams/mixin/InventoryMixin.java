package com.t2pellet.teams.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.menu.TeamsLonelyScreen;
import com.t2pellet.teams.client.ui.menu.TeamsMainScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public class InventoryMixin extends Screen {

    // TODO : Fix team button position not updating when the recipe book is opened / closed

    private static final Identifier TEAMS_BUTTON_TEXTURE = new Identifier(TeamsMod.MODID, "textures/gui/buttonsmall.png");

    @Shadow private float mouseX;
    @Shadow private float mouseY;

    protected InventoryMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
        if (!TeamsModClient.client.interactionManager.hasCreativeInventory()) {
            InventoryAccessor screen = ((InventoryAccessor) ((Object) this));
            addDrawableChild(new TexturedButtonWidget(screen.getX() + screen.getBackgroundWidth() - 19, screen.getY() + 4, 15, 14, 0, 0, 13, TEAMS_BUTTON_TEXTURE, (button) -> {
                if (ClientTeam.INSTANCE.isInTeam()) {
                    TeamsModClient.client.setScreen(new TeamsMainScreen(TeamsModClient.client.currentScreen));

                } else {
                    TeamsModClient.client.setScreen(new TeamsLonelyScreen(TeamsModClient.client.currentScreen));
                }
            }));
        }
    }
}
