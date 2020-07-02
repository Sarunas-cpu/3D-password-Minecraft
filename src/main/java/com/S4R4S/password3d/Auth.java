package com.S4R4S.password3d;

import java.util.ArrayList;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Auth {
	// event list
	private static ArrayList<String> events;
	// event location
	private static ArrayList<BlockPos> eventLocs;
	// block position
	private static ArrayList<BlockPos> blocksLocs;
	// item in hand
	private static ArrayList<String> itemsInHand;
	// back space arrays to remove eventType
	public static ArrayList<String> eventTypes = new ArrayList<>();
	// back space arrays position to be removed or added to
	private static ArrayList<BlockPos> eventPoss = new ArrayList<>();
	// back space array for block state
	private static ArrayList<BlockState> eventBlockStates = new ArrayList<>();
	
	public Auth(ArrayList<String> events, ArrayList<BlockPos> eventLocs, ArrayList<BlockPos> blocksLoc, ArrayList<String> itemsInHand) {
		
		Auth.events = new ArrayList<>(events);
		Auth.eventLocs = new ArrayList<>(eventLocs);
		Auth.blocksLocs = new ArrayList<>(blocksLoc);
		Auth.itemsInHand = new ArrayList<>(itemsInHand);
		
//		eventTypes = new ArrayList<>();
//		eventPoss = new ArrayList<>();
//		eventBlockStates = new ArrayList<>();
	}
	/* Check if even is the same event as in a password
	 * also check if the location is the same
	 * @param event event that has been triggered
	 * @param eventLoc location of that event
	 * 
	 * @return true if the location and event matches else false
	 */
	public boolean check(String event, BlockPos eventLoc, BlockPos blockLoc, String itemInHand) {
		System.out.println(events);
		if(events.get(0) == null) {
			return false;
		}
		if(events.get(0).equals("enter") && eventLoc == null) {
			removeEvent();
			return true;
		}
		if(blockLoc == null && itemInHand == null) {
			if(event.equals(events.get(0))) {
				if(checkPos(eventLocs.get(0), eventLoc)) {
					removeEvent();
					return true;
				}
			}
		} else if(event.equals(events.get(0)) && itemsInHand.get(0).equals(itemInHand)) {
			if(checkPos(eventLocs.get(0), eventLoc)) {
				if(checkPoEx(blocksLocs.get(0), blockLoc)) {
					removeEvent();
					return true;
				}
			}
		}		
		return false;
	}
	/*
	 * @param first block position
	 * @param second block position
	 * 
	 * @return true if the position is within range 
	 */
	public boolean checkPos(BlockPos x, BlockPos y)
	{		
		if(x.getX() - 2 <= y.getX() && x.getX() + 2 >= y.getX()) {
			if(x.getY() - 2 <= y.getY() && x.getY() + 2 >= y.getY()) {
				if(x.getZ() - 2 <= y.getZ() && x.getZ() + 2 >= y.getZ()) {
					return true;
				}	
			}	
		}
		return false;
	}
	/*
	 * @param first block position
	 * @param second block position
	 * 
	 * @return true if the position of the block is exact match
	 */
	public boolean checkPoEx(BlockPos x, BlockPos y)
	{		
		if(x.getX() == y.getX()) {
			if(x.getY() == y.getY()) {
				if(x.getZ() == y.getZ()) {
					return true;
				}	
			}	
		}
		return false;
	}
	
	/*
	 * add wrong event to the array
	 */
	public void addBadEvent() {
		events.add(0, null);
	}
	/*
	 * add events to be removed
	 */
	public static void addEventType(String eventType, BlockPos eventPos, BlockState eventBlockState) {
		System.out.println("Event has been added " + eventType);
		eventTypes.add(eventType);
		eventPoss.add(eventPos);
		eventBlockStates.add(eventBlockState);
		
	}
	/*
	 * remove event typ
	 */
	private static void removeEventType() {
		eventTypes.remove(eventTypes.size() - 1);
		eventPoss.remove(eventPoss.size() - 1);
		eventBlockStates.remove(eventBlockStates.size() - 1);		
	}
	
	
	/*
	 * Remove first event  and location from the lists
	 */
	public static void removeEvent() {
			events.remove(0);
			eventLocs.remove(0);
			blocksLocs.remove(0);
			itemsInHand.remove(0);
	}

	/*
	 * remove event on backspace key pressed
	 */
	public static boolean removeEventOnBackspace() {
		PasswordPlayer tempPlayer = Password3D.cmd.getPasswordPlayer();
		if (tempPlayer != null && !eventTypes.isEmpty()) {
			if (!Password3D.cmd.getpassBeenSet()) {
				if (events.get(0) == null) {
					events.remove(0);
				} else if (tempPlayer.getEvents().size() > events.size()) {
					int newPassSize = events.size() + 1;

					events = new ArrayList<>(tempPlayer.getEvents());
					eventLocs = new ArrayList<>(tempPlayer.getEventsLoc());
					blocksLocs = new ArrayList<>(tempPlayer.getBlocksLoc());
					itemsInHand = new ArrayList<>(tempPlayer.getItemsInHand());

					while (events.size() > newPassSize) {
						System.out.println("something is happening");
						removeEvent();
					}
				}
			}


			System.out.println(eventTypes);

			if (eventTypes.get(eventTypes.size() - 1).equals("playerPlaceEvent")) {
				Password3D.player.world.destroyBlock(eventPoss.get(eventTypes.size() - 1), false);
//					Minecraft.getInstance().world.getBlockState(eventPoss.get(0)).notify();
				removeEventType();
			} else if (eventTypes.get(eventTypes.size() - 1).equals("playerDestEvent")) {
				Password3D.player.world.setBlockState(eventPoss.get(eventPoss.size() - 1),
						eventBlockStates.get(eventBlockStates.size() - 1));
//					Minecraft.getInstance().world.getBlockState(eventPoss.get(eventPoss.size() - 1)).notify();
				removeEventType();
			}
			if(Password3D.cmd.getpassBeenSet()) {
				Password3D.cmd.getPasswordPlayer().deleteLastEvent();
			}
			return true;
		} else {
			return false;
		}

	}
	/*
	 * reset the arrays
	 */
	public void reset() {
		events.clear();
		eventLocs.clear();
		blocksLocs.clear();
		itemsInHand.clear();
	}
	/*
	 * Return list of events
	 */
	public ArrayList<String> getEvents() {
		return events;
	}
	/*
	 * return list of event locations
	 */
	public ArrayList<BlockPos> getLocs() {
		return eventLocs;
	}
	
	public int eventsLeft() {
		return events.size();
	}

}
