package com.S4R4S.password3d;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class PasswordProgressWindow extends Screen {
		
		protected PasswordProgressWindow() {
			super(new StringTextComponent("password setup"));
		}
		
		@Override
		public boolean shouldCloseOnEsc() {
			return false;
		}
		
		
		@Override
		public boolean isPauseScreen() {
			return false;
		}

		@Override
		public void init() {
			super.init();
			buttons.clear();
		}
		/*
		 * (non-Javadoc)
		 * @see net.minecraft.client.gui.screen.Screen#render(int, int, float)
		 * Render text on screen and text input fields
		 */
		@Override
		public void render(int par1, int par2, float par3) {
			drawCenteredString(font, "Password setup is in progress", width / 2, height - 95, 0xC54C4B);
	        super.render(par1, par2, par3);
		}



		@Override
		public void onClose() {
			super.onClose();
		}
}
