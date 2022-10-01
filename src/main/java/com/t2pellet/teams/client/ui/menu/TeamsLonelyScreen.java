package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.TeamsMod;
import com.t2pellet.teams.client.core.ClientTeamDB;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

public class TeamsLonelyScreen extends TeamsScreen {

    static final int WIDTH = 256;
    static final int HEIGHT = 166;
    private static final Identifier TEXTURE = new Identifier(TeamsMod.MODID, "textures/gui/screen_background.png");
    private static final Text CREATE_TEXT = MutableText.of(new LiteralTextContent("teams.menu.create"));
    private static final Text LONELY_TEXT = MutableText.of(new LiteralTextContent("teams.menu.lonely.alone"));
    private static final Text GO_BACK_TEXT = MutableText.of(new LiteralTextContent("teams.menu.return"));

    public TeamsLonelyScreen(Screen parent) {
        super(parent, MutableText.of(new LiteralTextContent("teams.menu.lonely.title")));
    }

    @Override
    protected void init() {
        super.init();
        // Team Entries
        int yPos = y + 12;
        for (String team : ClientTeamDB.INSTANCE.getOnlineTeams()) {
            var entry = new TeamEntry(team, this.width / 2 - 122, yPos);
            addDrawableChild(entry);
            addSelectableChild(entry.joinButton);
            yPos += 24;
        }
        // Menu buttons
        addDrawableChild(new ButtonWidget(this.width / 2 - 106, y + HEIGHT - 30, 100, 20, CREATE_TEXT, button -> {
            client.setScreen(new TeamsCreateScreen(this));
        }));
        addDrawableChild(new ButtonWidget(this.width / 2 + 6, y + HEIGHT - 30, 100, 20, GO_BACK_TEXT, button -> {
            client.setScreen(parent);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (ClientTeamDB.INSTANCE.getOnlineTeams().isEmpty()) {
            int textWidth = textRenderer.getWidth(LONELY_TEXT);
            int textHeight   = textRenderer.fontHeight;
            textRenderer.draw(matrices, LONELY_TEXT, (int) ((this.width - textWidth) / 2), y + 24 - (int) (textHeight / 2), Color.BLACK.getRGB());
        }
    }

    public void refresh() {
        client.setScreen(new TeamsLonelyScreen(parent));
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
        return 1.0F;
    }
}
