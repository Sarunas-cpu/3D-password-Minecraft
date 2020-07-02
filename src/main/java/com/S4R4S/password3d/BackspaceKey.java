package com.S4R4S.password3d;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BackspaceKey extends KeyBinding {
	
	   public BackspaceKey(String description, int keyCode, String category) {
		   super("key.undoAction", 259, "key.categories.misc");
	}

	@SubscribeEvent()
	   public void onKeyInput(TickEvent.ClientTickEvent event)
	   {
	      if(event.phase == TickEvent.Phase.END && this.isKeyDown())
	      {
	      }
	   }

}
