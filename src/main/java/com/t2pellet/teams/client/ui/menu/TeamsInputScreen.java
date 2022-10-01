package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.TeamsMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class TeamsInputScreen extends TeamsScreen {

    private static final Identifier TEXTURE = new Identifier(TeamsMod.MODID, "textures/gui/smaller_background.png");
    private static final Text DEFAULT_TEXT = Text.translatable("teams.menu.input");
    private static final Text GO_BACK_TEXT = Text.translatable("teams.menu.return");
    private static final int WIDTH = 120;
    private static final int HEIGHT = 110;

    protected TextFieldWidget inputField;
    protected ButtonWidget submitButton;
    private String prevInputText = "";

    public TeamsInputScreen(Screen parent, Text title) {
        super(parent, title);
    }

    @Override
    protected void init() {
        super.init();
        inputField = addDrawableChild(new TextFieldWidget(client.textRenderer, x + (getWidth() - 100) / 2, y + 10, 100, 20, DEFAULT_TEXT));
        submitButton = addDrawableChild(new ButtonWidget(x + (getWidth() - 100) / 2, y + HEIGHT - 55, 100, 20, getSubmitText(), this::onSubmit));
        submitButton.active = submitCondition();
        addDrawableChild(new ButtonWidget(x + (getWidth() - 100) / 2, y + HEIGHT - 30, 100, 20, GO_BACK_TEXT, button -> {
            client.setScreen(parent);
        }));
    }

    @Override
    public void tick() {
        if (!prevInputText.equals(inputField.getText())) {
            submitButton.active = submitCondition();
        }
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

    protected abstract Text getSubmitText();

    protected abstract void onSubmit(ButtonWidget widget);

    protected abstract boolean submitCondition();

}
