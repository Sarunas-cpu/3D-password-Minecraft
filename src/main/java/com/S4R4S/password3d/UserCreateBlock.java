package com.S4R4S.password3d;

import java.io.Serializable;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UserCreateBlock extends Block implements Serializable{

	public UserCreateBlock() {
		super(Properties.create(Material.IRON).hardnessAndResistance(2.0f));
		setRegistryName("usercreate");
	}
	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
    	if(!player.world.isRemote() && Password3D.cmd.getPasswordPlayer() == null) {
    		Minecraft.getInstance().displayGuiScreen(new UserCreateColourWindow());
    	}
		super.onBlockClicked(state, worldIn, pos, player);
	}
	
	
}