package com.S4R4S.password3d;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderHandler {
    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event)
    {	
    	int width = event.getWindow().getScaledWidth()/3;
    	if(Password3D.cmd.getpassBeenSet()) {
    		Minecraft.getInstance().fontRenderer.drawString("Password setup is in progress",  width, 50, 0xC54C4B);
    	}
    	if(Password3D.cmd.getPasswordPlayer() != null && Password3D.cmd.getPasswordPlayer().isPasswordLocked() && !Password3D.playerLoginStatus) {
    		Minecraft.getInstance().fontRenderer.drawString("Please enter your password",  width, 50, 0x00FF00);   		
    	}
    	if(Password3D.cmd.getPasswordPlayer() != null && !Password3D.cmd.getPasswordPlayer().isPasswordLocked() && !Password3D.cmd.getpassBeenSet()) {
    		Minecraft.getInstance().fontRenderer.drawString("Please create a password",  width, 50, 0x00FFFF);   		
    	}

    }
}
