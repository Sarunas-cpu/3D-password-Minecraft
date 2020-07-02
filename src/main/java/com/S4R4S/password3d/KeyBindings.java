package com.S4R4S.password3d;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
	
	
	 public static KeyBinding backspace;
	 public static KeyBinding enter;
	 public static KeyBinding esc;
	 
	    public static void register()
	    {
	        backspace = new KeyBinding("key.backspace", 259, "key.categories.misc");
	        enter = new KeyBinding("key.enter", 257, "key.categories.misc");
	        esc = new KeyBinding("key.esc", 256, "key.categories.misc");
	 
	        ClientRegistry.registerKeyBinding(backspace);
	        ClientRegistry.registerKeyBinding(enter);
	        ClientRegistry.registerKeyBinding(esc);
	    }

}
