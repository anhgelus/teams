package com.t2pellet.teams.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.t2pellet.teams.TeamsMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
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
public class InventoryMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

    private static final Identifier TEAMS_BUTTON_TEXTURE = new Identifier(TeamsMod.MODID, "textures/gui/buttonsmall.png");

    @Shadow private float mouseX;
    @Shadow private float mouseY;

    public InventoryMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
        if (!MinecraftClient.getInstance().interactionManager.hasCreativeInventory()) {
            addDrawableChild(new TexturedButtonWidget(this.x + 157, this.y + 4, 15, 14, 0, 0, 13, TEAMS_BUTTON_TEXTURE, (button) -> {
                // TODO : Open Teams GUI
            }));
        }
    }

    // Identical to the original, just needed it so I could extend AbstractInventoryScreen and get access to the x, y fields
    @Override
    public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int i = this.x;
        int j = this.y;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        InventoryScreen.drawEntity(i + 51, j + 75, 30, (float)(i + 51) - this.mouseX, (float)(j + 75 - 50) - this.mouseY, this.client.player);
    }
}
