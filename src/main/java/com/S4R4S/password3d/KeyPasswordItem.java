package com.S4R4S.password3d;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class KeyPasswordItem extends Item{
	// current player logged in
	private static PlayerEntity player;
	// counter for click ticks
	private int counter;
	// if the password is being set or cleared
	private boolean setPass;
	// current setCommands for current passwordplayer
	private SetCommands cmd;
	
	public KeyPasswordItem() {
		super(new Item.Properties());
		setRegistryName("keypassword");
		setPass = true;
		counter = 0;
	}
	/*
	 * On first item right click the user is able to set the password on second click the password is saved
	 * @see net.minecraft.item.Item#onItemRightClick(net.minecraft.world.World, net.minecraft.entity.player.PlayerEntity, net.minecraft.util.Hand)
	 */

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    	if(cmd.getPasswordPlayer() != null) {
    		
//    		player.sendMessage(new StringTextComponent(Password3D.cmd.getPasswordPlayer().getEvents().toString()));
			counter++;
	    	if(counter == 2) {
	    		if(Password3D.playerLoginStatus && setPass) {
	    			if(Password3D.cmd.getPasswordPlayer().getEvents().isEmpty()) {
		    			player.getServer().getCommandManager().handleCommand(player.getCommandSource(), "/setPass");
			        	counter = 0;	
			        	setPass = false;
	    			} else {
		    			player.getServer().getCommandManager().handleCommand(player.getCommandSource(), "/clr");
		    			player.getServer().getCommandManager().handleCommand(player.getCommandSource(), "/setPass");
			        	counter = 0;
			        	setPass = false;
	    			} 
	    		} else if(counter == 2 && Password3D.playerLoginStatus && !setPass && !Password3D.cmd.getPasswordPlayer().getEvents().isEmpty()) {
		    			player.getServer().getCommandManager().handleCommand(player.getCommandSource(), "/save");
		    			counter = 0;
		    			setPass = true;
		    	} else if(counter == 2 && !setPass && Password3D.cmd.getPasswordPlayer().getEvents().isEmpty()) {
		    			player.sendMessage(new StringTextComponent("Please perform action to save password"));
		    			counter = 0;
		    	} else if(counter == 2 && !Password3D.playerLoginStatus){
		    			player.sendMessage(new StringTextComponent("Please login first to set up a new password"));
		    			counter = 0;
		    	}	    	
	    	}
	}
    	
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	public void  setPlayer(PlayerEntity player) {
		this.player = player;
	}
	
	public void setSetPass(boolean setPass) {
		this.setPass = setPass;
	}

	public void setCmd(SetCommands cmd) {
		this.cmd = cmd;
	}
}
