package com.S4R4S.password3d;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class KeyCharInputItem extends Item {
	// current player logged in
		private static PlayerEntity player;
		// current setCommands for current passwordplayer
		private SetCommands cmd;
		
		public KeyCharInputItem() {
			super(new Item.Properties());
			setRegistryName("keycharinput");
		}
		/*
		 * On first item right click the user is able to set the password on second click the password is saved
		 * @see net.minecraft.item.Item#onItemRightClick(net.minecraft.world.World, net.minecraft.entity.player.PlayerEntity, net.minecraft.util.Hand)
		 */

		@Override
		public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
	    	if(cmd.getPasswordPlayer() != null) {
		    	if(!player.world.isRemote()) {
		    		Minecraft.getInstance().displayGuiScreen(new CharInputWindow(player, cmd));
		    	}
	    	}
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
		
		public void  setPlayer(PlayerEntity player) {
			this.player = player;
		}
		
		public void setCmd(SetCommands cmd) {
			this.cmd = cmd;
		}
}
