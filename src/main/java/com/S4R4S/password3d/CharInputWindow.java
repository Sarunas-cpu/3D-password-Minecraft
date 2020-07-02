package com.S4R4S.password3d;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class CharInputWindow extends Screen {
	private TextFieldWidget username;
	// current player
	private PlayerEntity player;
	// commands
	private SetCommands cmd;

	
	protected CharInputWindow(PlayerEntity player, SetCommands cmd) {
		super(new StringTextComponent("char input event"));
		this.cmd = cmd;
		this.player = player;
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
	
	
	@Override
	public boolean isPauseScreen() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.screen.Screen#init()
	 * Create text field to enter characters for the password and confirm button 
	 */
	@Override
	public void init() {
		super.init();
		buttons.clear();
		
		username = new TextFieldWidget(font, width / 2 - 100, height - 80, 200, 20, "Username");
        username.setFocused2(true);		
		username.setMaxStringLength(20);
		username.setVisible(true);
		children.add(username);
		// Confirm button
		addButton(new Button(width / 2 - 100, height - 50, 200, 20, "Confirm", (button) -> onClose()));
	}
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.screen.Screen#render(int, int, float)
	 * Render text on screen and text input fields
	 */
	@Override
	public void render(int par1, int par2, float par3) {
		drawCenteredString(font, "Enter characters you want to add:", width / 2, height - 95, 0xFFFFFF);
		username.render(par1, par2, par3);
        super.render(par1, par2, par3);
	}

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		if(username.isFocused()) {
			username.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
		return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}	
	
	@Override
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		if(username.isFocused()) {
			username.charTyped(p_charTyped_1_, p_charTyped_2_);
		}
		if(KeyBindings.enter.isPressed()) {
			onClose();
		}
		return super.charTyped(p_charTyped_1_, p_charTyped_2_);
	}
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.screen.Screen#onClose()
	 * Checks if the event exist in the event list and removes it if it does
	 */

	@Override
	public void onClose() {
		Password3D.addAndCheckEvent(username.getText(), player.getPosition(), null, null);
		super.onClose();
	
	}
}
