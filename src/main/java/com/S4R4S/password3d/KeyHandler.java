package com.S4R4S.password3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyHandler {
//    static int counter = 0;
	/*
	 * to delete password events and finalise password entering 257 = enter button
	 * 259 = back space
	 */
	@SubscribeEvent
	public void keyPressed(InputEvent.KeyInputEvent keyEvent) {
		if (keyEvent.getKey() == 257 && (Minecraft.getInstance().currentScreen instanceof UserCreateWindow
				|| Minecraft.getInstance().currentScreen instanceof CharInputWindow)) {
			Minecraft.getInstance().currentScreen.onClose();
		}
		System.out.println("###########");
		if (KeyBindings.enter.isPressed()) {
			if (Password3D.cmd.getpassBeenSet()) {
				Password3D.key.onItemRightClick(Password3D.player.world, Password3D.player,
						Password3D.player.getActiveHand());
				Password3D.key.onItemRightClick(Password3D.player.world, Password3D.player,
						Password3D.player.getActiveHand());
			} else if (Password3D.checkEvent != null && Password3D.checkEvent.check("enter", null, null, null)) {
				// initialise the password again
				if (Password3D.checkEvent.eventsLeft() == 0) {
					Password3D.player.getServer().getCommandManager()
							.handleCommand(Password3D.player.getServer().getCommandSource(), "/worldborder set 600000");
					Password3D.player.sendMessage(new StringTextComponent("You successfully completed the pattern"));
					Password3D.player.getServer().getCommandManager()
							.handleCommand(Password3D.player.getServer().getCommandSource(), "/tp Dev ");
					Password3D.checkEvent = new Auth(Password3D.cmd.passwordPlayer.getEvents(),
							Password3D.cmd.passwordPlayer.getEventsLoc(), Password3D.cmd.passwordPlayer.getBlocksLoc(),
							Password3D.cmd.passwordPlayer.getItemsInHand());
					Password3D.player.getServer().getCommandManager().handleCommand(
							Password3D.player.getCommandSource(),
							"/tp " + Password3D.cmd.passwordPlayer.getPos().getX() + " "
									+ Password3D.cmd.passwordPlayer.getPos().getY() + " "
									+ Password3D.cmd.passwordPlayer.getPos().getZ());
					Password3D.playerLoginStatus = true;
				}
			}
		}

		System.out.println(keyEvent.getKey());
		if (KeyBindings.backspace.isPressed() && !Auth.eventTypes.isEmpty()) {
			System.out.println("Something is happening");
			if (Auth.removeEventOnBackspace()) {
				Password3D.player.sendMessage(new StringTextComponent("Last event have been deleted"));
			}

		}
		if(KeyBindings.esc.isPressed()) {
			System.out.println("is pressed '''''''''");
		}
	}
}
