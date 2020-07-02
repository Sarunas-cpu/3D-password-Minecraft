package com.S4R4S.password3d;

import java.io.Serializable;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UserBlock extends Block implements Serializable{
	// block name
	private String name;


	public UserBlock(String name) {
		super(Properties.create(Material.IRON).hardnessAndResistance(2.0f));
		setRegistryName(name);
		this.name = name;
//		userBlocks.add(this);
	}
	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
    	if(!player.world.isRemote() && Password3D.cmd.getPasswordPlayer() == null) {
        	if(Password3D.cmd.getPlayerList().containsKey(name)) {
        		player.getServer().getCommandManager().handleCommand(player.getCommandSource(), "/login " + name);
        		if(Password3D.cmd.getPasswordPlayer().getEvents().isEmpty()) {
        			Password3D.playerLoginStatus = true;
        		}
        		Password3D.giveItems();
        	}
    	}
		super.onBlockClicked(state, worldIn, pos, player);
	}
	
	public String getName() {
		return name;
	}	
	
}
