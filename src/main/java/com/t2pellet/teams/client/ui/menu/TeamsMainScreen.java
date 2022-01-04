package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamLeavePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TeamsMainScreen extends TeamsScreen {

    static final int WIDTH = 267;
    static final int HEIGHT = 183;
    private static final Identifier TEXTURE = new Identifier(TeamsMod.MODID, "textures/gui/screen_background.png");
    private static final Text INVITE_TEXT = new TranslatableText("teams.menu.invite");
    private static final Text LEAVE_TEXT = new TranslatableText("teams.menu.leave");
    private static final Text GO_BACK_TEXT = new TranslatableText("teams.menu.return");

    public TeamsMainScreen(Screen parent) {
        super(parent, new TranslatableText("teams.menu.title"));
    }

    @Override
    protected void init() {
        super.init();
        int yPos = y + 12;
        int xPos = x + (WIDTH - TeammateEntry.WIDTH) / 2;
        // Add player buttons
        for (var teammate : ClientTeam.INSTANCE.getTeammates()) {
            boolean local = client.player.getUuid().equals(teammate.id);
            var entry = new TeammateEntry(this, teammate, xPos, yPos, local);
            addDrawableChild(entry);
            if (entry.getFavButton() != null) {
                addSelectableChild(entry.getFavButton());
            }
            if (entry.getKickButton() != null) {
                addSelectableChild(entry.getKickButton());
            }
            yPos += 24;
        }
        // Add menu buttons
        addDrawableChild(new ButtonWidget(this.width / 2  - 125, y + HEIGHT - 30, 80, 20, LEAVE_TEXT, button -> {
            PacketHandler.INSTANCE.sendToServer(new TeamLeavePacket(client.player.getUuid()));
            client.setScreen(new TeamsLonelyScreen(parent));
        }));
        addDrawableChild(new ButtonWidget(this.width / 2  - 40, y + HEIGHT - 30, 80, 20, INVITE_TEXT, button -> {
            client.setScreen(new TeamsInviteScreen(this));
        }));
        addDrawableChild(new ButtonWidget(this.width / 2  + 45, y + HEIGHT - 30, 80, 20, GO_BACK_TEXT, button -> {
            client.setScreen(parent);
        }));
    }

    @Override
    protected int getWidth() {
        return WIDTH;
    }

    @Override
    protected int getHeight() {
        return HEIGHT;
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    protected float getBackgroundScale() {
        return 1.1F;
    }

    public void refresh() {
        if (!ClientTeam.INSTANCE.isInTeam()) {
            client.setScreen(parent);
        } else {
            client.setScreen(new TeamsMainScreen(parent));
        }
    }

}
