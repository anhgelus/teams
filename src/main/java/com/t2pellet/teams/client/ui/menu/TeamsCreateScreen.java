package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.client.core.ClientTeamDB;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamCreatePacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class TeamsCreateScreen extends TeamsInputScreen {

    private static final Text CREATE_TITLE = MutableText.of(new LiteralTextContent("teams.menu.create.title"));
    private static final Text CREATE_TEXT = MutableText.of(new LiteralTextContent("teams.menu.create.text"));

    public TeamsCreateScreen(Screen parent) {
        super(parent, CREATE_TITLE);
    }

    @Override
    protected Text getSubmitText() {
        return CREATE_TEXT;
    }

    @Override
    protected void onSubmit(ButtonWidget widget) {
        client.setScreen(new TeamsMainScreen(null));
        PacketHandler.INSTANCE.sendToServer(new TeamCreatePacket(inputField.getText(), client.player.getUuid()));
    }

    @Override
    protected boolean submitCondition() {
        return !ClientTeamDB.INSTANCE.containsTeam(inputField.getText());
    }
}
