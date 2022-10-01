package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.toast.ToastInviteSent;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamInvitePacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class TeamsInviteScreen extends TeamsInputScreen {

    private static final Text TITLE_TEXT = Text.translatable("teams.menu.invite.title");
    private static final Text INVITE_TEXT = Text.translatable("teams.menu.invite.text");


    public TeamsInviteScreen(Screen parent) {
        super(parent, TITLE_TEXT);
    }

    @Override
    protected float getBackgroundScale() {
        return 1.0F;
    }

    @Override
    protected Text getSubmitText() {
        return INVITE_TEXT;
    }

    @Override
    protected void onSubmit(ButtonWidget widget) {
        PacketHandler.INSTANCE.sendToServer(new TeamInvitePacket(client.player.getUuid(), inputField.getText()));
        client.getToastManager().add(new ToastInviteSent(ClientTeam.INSTANCE.getName(), inputField.getText()));
        client.setScreen(parent);
    }

    @Override
    protected boolean submitCondition() {
        String clientName = client.player.getName().getString();
        return client.getNetworkHandler().getPlayerList()
                .stream()
                .anyMatch(entry -> !entry.getProfile().getName().equals(clientName) && entry.getProfile().getName().equals(inputField.getText()));
    }
}
