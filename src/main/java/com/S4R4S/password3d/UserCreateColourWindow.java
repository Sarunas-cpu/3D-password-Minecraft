package com.S4R4S.password3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class UserCreateColourWindow extends Screen {

	
	protected UserCreateColourWindow() {
		super(new StringTextComponent("create colour"));
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
	 * Create buttons to select colour
	 */
	@Override
	public void init() {
		super.init();
		buttons.clear();
		
		// Colour buttons
		addButton(new Button(width / 2 - 100, height - 145, 98, 20, "Blue", (button) -> onClose("Blue")));
		addButton(new Button(width / 2 + 1, height - 145, 98, 20, "Yellow", (button) -> onClose("Yellow")));
		
		addButton(new Button(width / 2 - 100, height - 170, 98, 20, "Pink", (button) -> onClose("Pink")));
		addButton(new Button(width / 2 + 1, height - 170, 98, 20, "Green", (button) -> onClose("Green")));
		
		addButton(new Button(width / 2 - 100, height - 195, 98, 20, "Purple", (button) -> onClose("Purple")));
		addButton(new Button(width / 2 + 1, height - 195, 98, 20, "Orange", (button) -> onClose("Orange")));
		
		addButton(new Button(width / 2 - 100, height - 120, 98, 20, "Cyan", (button) -> onClose("Cyan")));
	}
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.screen.Screen#render(int, int, float)
	 * Render text on screen and text input fields
	 */
	@Override
	public void render(int par1, int par2, float par3) {
		drawCenteredString(font, "Select user colour:", width / 2, height - 220, 0xFFFFFF);
        super.render(par1, par2, par3);
	}

	public void onClose(String colour) {	
		super.onClose();
		Minecraft.getInstance().displayGuiScreen(new UserCreateWindow(colour));
	}

}
