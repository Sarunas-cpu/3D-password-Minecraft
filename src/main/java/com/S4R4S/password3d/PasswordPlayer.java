package com.S4R4S.password3d;

import java.io.Serializable;
import java.util.ArrayList;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class PasswordPlayer implements Serializable{

    // events that for 3D password
    private ArrayList<String> events;
    // position of the player at the time of the event
    private ArrayList<String> eventsLoc;
    // block location
    private ArrayList<String> blocksLoc;
    // item in hand
    private  ArrayList<String> itemsInHand;
    // password status to lock
    private boolean passwordLocked;
    // Entity player to send messages
    private String playerName;
    // BlockPos player position on logging out
    private String playerPos;
    // player ID
    private String passwordID;
    
	public PasswordPlayer(String passwordID) {
		   this.passwordID = passwordID;
		   playerName = null;
	       passwordLocked = false;
	       eventsLoc = new ArrayList<>();
	       events = new ArrayList<>();
	       blocksLoc = new ArrayList<>();
	       itemsInHand= new ArrayList<>();
	}
	
	public void deleteLastEvent() {
		if(!events.isEmpty()) {
			events.remove(events.size()-1);
			eventsLoc.remove(eventsLoc.size()-1);
			blocksLoc.remove(blocksLoc.size()-1);
			itemsInHand.remove(itemsInHand.size()-1);
			
		}
	}
	
	public void setEndEvent() {
		events.add("enter");
		eventsLoc.add(null);
		blocksLoc.add(null);
		itemsInHand.add(null);
	}
	/*
	 * get password id
	 */
	public String getPasswordID() {
		return passwordID;
	}	
	/*
	 * Set password name
	 */
	public void setName(String name) {
		playerName = name;
	}
	
	/*
	 * clears the password
	 */
	public void clearPassword() {
	       passwordLocked = false;
	       eventsLoc = new ArrayList<>();
	       events = new ArrayList<>();
	       blocksLoc = new ArrayList<>();
	       itemsInHand= new ArrayList<>();
	}
	/*
	 * Updates player position
	 * @BlockPos player position on log out
	 */
	public void updatePosition(BlockPos newPos) {
		playerPos = newPos.getX()+"|"+newPos.getY()+"|"+newPos.getZ();
		System.out.println(playerPos+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	}
	/*
	 * @return current player postion
	 */
	public BlockPos getPos() {
		if(playerPos == null) {
			return new BlockPos(18,69,235);
		}
		String[] pos = playerPos.split("\\|");
		System.out.println(pos[0] + " " + pos[1] + " " + pos[2] + "#############################################");
		return new BlockPos(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2]));
	}
	/*
	 * Adds EventLocation
	 */
	public void addEventLoc(BlockPos pos) {
		String loc = null;
		if(pos != null) {
			loc = pos.getX()+"|"+pos.getY()+"|"+pos.getZ();
		}
		eventsLoc.add(loc);
	}
	/*
	 * Adds BlockLocation
	 */
	public void addBlockLoc(BlockPos pos) {
		String loc = null;
		if(pos != null) {
			loc = pos.getX()+"|"+pos.getY()+"|"+pos.getZ();
		}
		blocksLoc.add(loc);
	}

	public ArrayList<String> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<String> events) {
		this.events = events;
	}

	public ArrayList<BlockPos> getEventsLoc() {
		ArrayList<BlockPos> locs = new ArrayList<>();
		for(String loc : eventsLoc) {
			if(loc == null) {
				locs.add(null);
			} else {
				String[] pos = loc.split("\\|");
				locs.add(new BlockPos(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2])));
			}

		}
		return locs;
	}

	public void setEventsLoc(ArrayList<BlockPos> eventsLoc) {
		ArrayList<String> locs = new ArrayList<>();
		for(BlockPos pos : eventsLoc) {
			if(pos == null) {
				locs.add(null);
			} else {
				String loc = pos.getX()+"|"+pos.getY()+"|"+pos.getZ();
				locs.add(loc);
			}
		}
		this.eventsLoc = locs;
	}

	public ArrayList<BlockPos> getBlocksLoc() {
		ArrayList<BlockPos> locs = new ArrayList<>();
		for(String loc : blocksLoc) {
			if(loc == null) {
				locs.add(null);
			} else {
				String[] pos = loc.split("\\|");
				locs.add(new BlockPos(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2])));
			}

		}
		return locs;
	}

	public void setBlocksLoc(ArrayList<BlockPos> blocksLoc) {
		ArrayList<String> locs = new ArrayList<>();
		for(BlockPos pos : blocksLoc) {
			if(pos == null) {
				locs.add(null);
			} else {
				String loc = pos.getX()+"|"+pos.getY()+"|"+pos.getZ();
				locs.add(loc);
			}

		}
		this.blocksLoc = locs;
	}

	public ArrayList<String> getItemsInHand() {
		return itemsInHand;
	}
	
	public String getName() {
		return playerName;
	}

	public void setItemsInHand(ArrayList<String> itemsInHand) {
		this.itemsInHand = itemsInHand;
	}

	public boolean isPasswordLocked() {
		return passwordLocked;
	}

	public void setPasswordLocked(boolean passwordLocked) {
		this.passwordLocked = passwordLocked;
	}

}
