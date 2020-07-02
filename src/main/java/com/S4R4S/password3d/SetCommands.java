package com.S4R4S.password3d;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Stack;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.NBTTextComponent.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.S4R4S.password3d.Password3D.RegistryEvents1;
import com.google.gson.Gson;
/*
 * This class is to set up all the needed commands for the password mod 
 * and identify the players to to log in
 */
public class SetCommands implements Serializable{
	
	private transient boolean passBeenSet;
	private transient boolean passCleared;
	private transient boolean loginCalled;
	private transient PlayerEntity player;
	public transient PasswordPlayer passwordPlayer;
	private transient String colour;
	// map usernames to player passwords
	private HashMap<String, PasswordPlayer> playerList;
	
	SetCommands() {	
		playerList = new HashMap<>();
		// initialise the players
		playerList.put("user1", null);
		playerList.put("user2", null);
		playerList.put("user3", null);
		playerList.put("user4", null);	
		playerList.put("user5", null);	
		playerList.put("user6", null);	
		playerList.put("user7", null);
		
		loginCalled = false;
		passBeenSet = false;
		passCleared = false;
	//	player = null;
		passwordPlayer = null;

	}
	/*
	 * sets CommandDispatcher
	 */
	public void setDisp(CommandDispatcher<CommandSource> disp) {
		setCommandSetPass(disp);
		setCommandSave(disp);
		setCommandClear(disp);
		setCommandLogin(disp);
		setCommandCreateUser(disp);
		setCommandDeleteUser(disp);
	}
	/*
	 * adds players to the list
	 */
	public void setPlayers() {
				
	}
	/*
	 * command to set the password
	 * when this command is executed the user starts recording his password
	 */
	private void setCommandSetPass(CommandDispatcher<CommandSource> disp) {
		
        disp.register(Commands.literal("setPass")
        		.executes(c -> {
        			if(passwordPlayer != null ) {
	 		        	player.sendMessage(new StringTextComponent("Perform password to be saved"));
			        	passBeenSet = true;       				
        			}  else {
        				player.sendMessage(new StringTextComponent("You need to login first"));
        			}
		            return 0;
		        })
        );
	}
	/*
	 *  command to save the password
	 *  when this command is executed the password is stopped to be recorded
	 */
	private void setCommandSave(CommandDispatcher<CommandSource> disp) {
		
        disp.register(Commands.literal("save")
        		.executes(c -> {
        			if(passwordPlayer != null ) {
        				player.sendMessage(new StringTextComponent("Password have been saved"));
        				passwordPlayer.setEndEvent();
						Password3D.checkEvent = new Auth(passwordPlayer.getEvents(), passwordPlayer.getEventsLoc(), passwordPlayer.getBlocksLoc(), passwordPlayer.getItemsInHand()); 
						passwordPlayer.setPasswordLocked(true);
						passBeenSet = false;
        			} else {
        				player.sendMessage(new StringTextComponent("You need to login first"));
        			}
		            return 0;
        		})
        );
	}
	/*
	 * sets command to clear password
	 * password of current player is cleared
	 */	
	private void setCommandClear(CommandDispatcher<CommandSource> disp) {
		
        disp.register(Commands.literal("clr")
        		.executes(c -> {
        			if(passwordPlayer != null) {
        			player.sendMessage(new StringTextComponent("Password has been cleared"));
		        	passCleared = true;
		        	passBeenSet = false;
		        	passwordPlayer.clearPassword();       				
        			} else {
        				player.sendMessage(new StringTextComponent("Please select the user to clear password"));
        			}

		            return 0;
        		})
        );
	}
	/*
	 * command to let the player login
	 * this command takes one parameter /login <username>
	 * if only /login is typer the user is promted to enter the username
	 */
	private void setCommandLogin(CommandDispatcher<CommandSource> disp) {
		
        disp.register(Commands.literal("login")
        		.then(Commands.argument("username", StringArgumentType.word())
        			.executes(c -> {
        				String playerKey = StringArgumentType.getString(c, "username");
        				if(playerList.containsKey(playerKey)) {
        					passwordPlayer = playerList.get(playerKey);
        					PasswordPlayer somePlayer = playerList.get(playerKey);
        					if(!passwordPlayer.isPasswordLocked()) {
        						player.sendMessage(new StringTextComponent("Please set up the password "+ passwordPlayer.getName()));
        						if(!player.inventory.hasItemStack(new ItemStack(new KeyPasswordItem(), 1))) {
        							player.addItemStackToInventory(new ItemStack(Password3D.key));
        							player.addItemStackToInventory(new ItemStack(Password3D.charKey));
        						}        					}
        					else if(!passwordPlayer.getEvents().isEmpty()) {
        						if(!player.inventory.hasItemStack(new ItemStack(new KeyPasswordItem(), 1))) {
        							player.addItemStackToInventory(new ItemStack(Password3D.key));
        							player.addItemStackToInventory(new ItemStack(Password3D.charKey));
        						}
        						
            					Password3D.checkEvent = new Auth(passwordPlayer.getEvents(), passwordPlayer.getEventsLoc(), passwordPlayer.getBlocksLoc(), passwordPlayer.getItemsInHand()); 
            					passwordPlayer.setPasswordLocked(true);
            					passBeenSet = false;
        				//	player.sendMessage(new StringTextComponent(playerList.toString()));
     //   					player.sendMessage(new StringTextComponent("Please enter your password " + somePlayer.getEvents().toString()));
        				//	player.sendMessage(new StringTextComponent("Please enter your password " + somePlayer.getName()));
        				//	player.sendMessage(new StringTextComponent("Please enter your password " + StringArgumentType.getString(c, "username")));
        					player.sendMessage(new StringTextComponent("Please enter your password " + passwordPlayer.getName()));
        				//	player.sendMessage(new StringTextComponent("Please enter your password " + playerList.get("user1").getName()))            					
        					}        					
        					loginCalled = true;
        				} else {
        					player.sendMessage(new StringTextComponent("User does not exist"));
        				}
        				return 1;
        			})
        		)
        		.executes(c -> {
        			player.sendMessage(new StringTextComponent("Please enter the username"));
        			return 1;
        		})
        );
	}
	
	
	/*
	 * command to create a new user and log ins into that user
	 * this command takes one parameter /create <username>
	 */
	private void setCommandCreateUser(CommandDispatcher<CommandSource> disp) {
		
        disp.register(Commands.literal("create")
        		.then(Commands.argument("username", StringArgumentType.word())
        			.executes(c -> {
        				if(passwordPlayer == null) {
	        				String playerKey = StringArgumentType.getString(c, "username");
	        				// find out if the user exists already
	        				boolean userExists = false;
	        				String availableUser = null;
	        				for(String user : playerList.keySet()) {
	        					if(playerList.get(user) != null) {
	        						String pwdName = playerList.get(user).getName();
		        					if(pwdName != null && pwdName.equals(playerKey)) {
		        						userExists = true;
		        					}
	        					} else {	        						
	        						availableUser = user;
	        					}
	        				}
	        				if(!userExists && availableUser != null) {
	        					
	        					playerList.remove(availableUser);
	        					String newUser = availableUser.substring(0,5) + createUserOnColour(colour);	
	        					PasswordPlayer newPlayer = new PasswordPlayer(newUser);
	        					playerList.put(newUser, newPlayer);
	        					newPlayer.setName(playerKey);
	        					System.out.println(newPlayer.getName()+" created");
//	        					player.sendMessage(new StringTextComponent(newPlayer.getName()+ " user has been created"));
	        					System.out.println("/give " + player.getScoreboardName() + "password3d:" + newPlayer.getPasswordID());
	        		        	player.getServer().getCommandManager().handleCommand(player.getCommandSource(), "/give " + player.getScoreboardName() + " password3d:" + newPlayer.getPasswordID());
	        					
	        					ItemStack temp = player.inventory.getStackInSlot(0);
	        					player.inventory.deleteStack(temp);	        					
	        					player.setHeldItem(player.getActiveHand(), temp);	        					        						
	        		  //      	player.getServer().getCommandManager().handleCommand(player.getCommandSource(), "/login " + newPlayer.getName());
	        				} else {
	        					if(userExists) {
	        						player.sendMessage(new StringTextComponent("User already exist"));
	        					}
        					if(availableUser == null) {
        						player.sendMessage(new StringTextComponent("Only 7 users can be created"));
    	        				for(String user : playerList.keySet()) {
    	        					String pwdName = playerList.get(user).getName();
    	        					player.sendMessage(new StringTextComponent(pwdName));
    	        				}
        					}
	        				}

        				} else {
        					player.sendMessage(new StringTextComponent("Please log out to create a new user"));
        				}
        				return 1;
        				})     			
        		)
        		.executes(c -> {
        			player.sendMessage(new StringTextComponent("Please enter the username to be created"));
        			return 1;
        		})
        );
	}
	
	public void setColour(String colour) {
		this.colour = colour;
	}
	
	public String createUserOnColour(String colour) {
		switch(colour) {
		  case "Blue":
			  return "1";
		  case "Yellow":
			  return "2";
		  case "Pink":
			  return "3";
		  case "Green":
			  return "4";
		  case "Orange":
			  return "5";
		  case "Purple":
			  return "6";
		  case "Cyan":
			  return "7"; 
		  default:
		    // code block
		}
		return "0";
		
	}
	/*
	 * command to let the player to be deleted
	 * this command takes one parameter /login <username>
	 * if only /login is typer the user is prompted to enter the username
	 */
	private void setCommandDeleteUser(CommandDispatcher<CommandSource> disp) {
		
        disp.register(Commands.literal("delete")
        		.then(Commands.argument("username", StringArgumentType.word())
        			.executes(c -> {
        				if(passwordPlayer == null) {
	        				String playerKey = StringArgumentType.getString(c, "username");
	        				// find out if the user exists already
	        				boolean userDeleted = false;
	        				PasswordPlayer newPlayer = null;
	        				for(String user : playerList.keySet()) {
	        					if(playerList.get(user) != null) {
	        						String pwdName = playerList.get(user).getName();
		        					if(pwdName.equals(playerKey)) {
		        						playerList.replace(user, null);
		        						userDeleted = true;
		        					}
	        					}

	        				}
	        				if(userDeleted) {
	        					player.sendMessage(new StringTextComponent("User has been deleted"));
	        					System.out.println(playerList.keySet());
	        				} else {
	        					player.sendMessage(new StringTextComponent("User has does not exist"));
	        				}

        				} else {
        					player.sendMessage(new StringTextComponent("Please log out to delete a user"));
        				}
        				return 1;
        				})     			
        		)
        		.executes(c -> {
        			player.sendMessage(new StringTextComponent("Please enter the username to be deleted"));
        			return 1;
        		})
        );
	}
	
	/*
	 * Gets the current player 
	 * @param PasswordPlayer
	 */
	public PasswordPlayer getPasswordPlayer() {
		return passwordPlayer;
	}	
	/*
	 * @return password state if it was set or not
	 */
	public  boolean getpassBeenSet() {
		return passBeenSet;
	}
	/*
	 * @return password state if it was set or not
	 */
	public boolean getLoginCalled() {
		return loginCalled;
	}
	/*
	 * reset login state
	 */
	public void resetLoginCalled() {
		loginCalled = false;
	}
	/*
	 * @return to see if password was cleared or not
	 */
	public boolean getClearState() {
		return passCleared;
	}
	/*
	 * resets the commands into initial state
	 */
	public void setPasswordLocked(boolean passwordLocked) {
		passBeenSet = passwordLocked;
	}
	public void reset() {
		passBeenSet = false;
		passCleared = false;
	}
	public void setPlayer(PlayerEntity player) {
		this.player = player;
		// TODO Auto-generated method stub
		
	}
	/*
	 * @return playerList
	 */	
	public HashMap<String, PasswordPlayer> getPlayerList() {
		return playerList;
	}
	
}
