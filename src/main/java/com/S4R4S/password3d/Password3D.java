package com.S4R4S.password3d;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftGame;
import net.minecraft.client.entity.player.*;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SDisconnectPacket;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.translation.*;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.TextComponent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.text.html.parser.Entity;



// The value here should match an entry in the META-INF/mods.toml file
@Mod("password3d")
public class Password3D
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    // base player
    public static PlayerEntity player;
    // server starting event
    private static FMLServerStartingEvent thisEvent;
    // SetCommand
    public static SetCommands cmd;
    // check if the event matches first event in the array(password)
    public static Auth checkEvent;
    // file to save SetCommandClass
    private static  File fileLoc = new File("DataPassword.txt"); 
    // key item to change and set password
    public static KeyPasswordItem key;
    // key item to add character event
    public static KeyCharInputItem charKey;
    // see if the player completed the pasword or not
    public static boolean playerLoginStatus;
    //arrays to save destroyed to replace them later on log out
    private static ArrayList<BlockState> destBlocks;
    // destroyed blocks position
    private static ArrayList<BlockPos> destBlocksPos;
    // arrays to save blocks positions that have been paced to be removed later on log out
    private static ArrayList<BlockPos> placedBlocksPos;
    // current block
    private static BlockState blockState;
    
    public Password3D() {
        // Register the setup method for modloading
       FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//        // Register ourselves for server and other game events we are interested in
       MinecraftForge.EVENT_BUS.register(this);
       MinecraftForge.EVENT_BUS.register(new RenderHandler());
       MinecraftForge.EVENT_BUS.register(new KeyHandler());
       MinecraftForge.EVENT_BUS.register(new RegistryEvents2());
       KeyBindings.register();
       
       key = new KeyPasswordItem();
       charKey = new KeyCharInputItem();
       
       playerLoginStatus = false;
       
       destBlocks = new ArrayList<>();
       destBlocksPos = new ArrayList<>();
       
       placedBlocksPos = new ArrayList<>();
    }
    
    private void setup(FMLCommonSetupEvent event)
    
    {     
     	// reads the date about the users from a file
    	try {
			//cmd = (SetCommands) new ObjectInputStream(new FileInputStream(fileLoc)).readObject();
    	       // Reading the object from a file 
       FileInputStream file = new FileInputStream(fileLoc); 
       ObjectInputStream object = new ObjectInputStream(file);
         
       cmd = (SetCommands) object.readObject(); 
         
       object.close(); 
       file.close();     		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(cmd == null) {
    		cmd = new SetCommands();
    		//cmd.setPlayers();
    	}
    	
    }
    // registering new blocks to the game
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    @ObjectHolder("password3d")
    public static class RegistryEvents1 {
    	public static final Block user11 = new UserBlock("user11");
    	public static final Block user12 = new UserBlock("user12");
    	public static final Block user13 = new UserBlock("user13");
    	public static final Block user14 = new UserBlock("user14");
    	public static final Block user15 = new UserBlock("user15");
    	public static final Block user16 = new UserBlock("user16");
    	public static final Block user17 = new UserBlock("user17");
    	
    	public static final Block user21 = new UserBlock("user21");
    	public static final Block user22 = new UserBlock("user22");
    	public static final Block user23 = new UserBlock("user23");
    	public static final Block user24 = new UserBlock("user24");
    	public static final Block user25 = new UserBlock("user25");
    	public static final Block user26 = new UserBlock("user26");
    	public static final Block user27 = new UserBlock("user27");
    	
    	public static final Block user31 = new UserBlock("user31");
    	public static final Block user32 = new UserBlock("user32");
    	public static final Block user33 = new UserBlock("user33");
    	public static final Block user34 = new UserBlock("user34");
    	public static final Block user35 = new UserBlock("user35");
    	public static final Block user36 = new UserBlock("user36");
    	public static final Block user37 = new UserBlock("user37");
    	
    	public static final Block user41 = new UserBlock("user41");
    	public static final Block user42 = new UserBlock("user42");
    	public static final Block user43 = new UserBlock("user43");
    	public static final Block user44 = new UserBlock("user44");
    	public static final Block user45 = new UserBlock("user45");
    	public static final Block user46 = new UserBlock("user46");
    	public static final Block user47 = new UserBlock("user47");
    	
    	public static final Block user51 = new UserBlock("user51");
    	public static final Block user52 = new UserBlock("user52");
    	public static final Block user53 = new UserBlock("user53");
    	public static final Block user54 = new UserBlock("user54");
    	public static final Block user55 = new UserBlock("user55");
    	public static final Block user56 = new UserBlock("user56");
    	public static final Block user57 = new UserBlock("user57");
    	
    	public static final Block user61 = new UserBlock("user61");
    	public static final Block user62 = new UserBlock("user62");
    	public static final Block user63 = new UserBlock("user63");
    	public static final Block user64 = new UserBlock("user64");
    	public static final Block user65 = new UserBlock("user65");
    	public static final Block user66 = new UserBlock("user66");
    	public static final Block user67 = new UserBlock("user67");
    	
    	public static final Block user71 = new UserBlock("user71");
    	public static final Block user72 = new UserBlock("user72");
    	public static final Block user73 = new UserBlock("user73");
    	public static final Block user74 = new UserBlock("user74");
    	public static final Block user75 = new UserBlock("user75");
    	public static final Block user76 = new UserBlock("user76");
    	public static final Block user77 = new UserBlock("user77");

    	public static final Block usercreate = new UserCreateBlock();
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        	// register users blocks
            event.getRegistry().register(user11);
            event.getRegistry().register(user12);
            event.getRegistry().register(user13);
            event.getRegistry().register(user14);
            event.getRegistry().register(user15);
            event.getRegistry().register(user16);
            event.getRegistry().register(user17);
            
            event.getRegistry().register(user21);
            event.getRegistry().register(user22);
            event.getRegistry().register(user23);
            event.getRegistry().register(user24);
            event.getRegistry().register(user25);
            event.getRegistry().register(user26);
            event.getRegistry().register(user27);
            
            event.getRegistry().register(user31);
            event.getRegistry().register(user32);
            event.getRegistry().register(user33);
            event.getRegistry().register(user34);
            event.getRegistry().register(user35);
            event.getRegistry().register(user36);
            event.getRegistry().register(user37);
            
            event.getRegistry().register(user41);
            event.getRegistry().register(user42);
            event.getRegistry().register(user43);
            event.getRegistry().register(user44);
            event.getRegistry().register(user45);
            event.getRegistry().register(user46);
            event.getRegistry().register(user47);
            
            event.getRegistry().register(user51);
            event.getRegistry().register(user52);
            event.getRegistry().register(user53);
            event.getRegistry().register(user54);
            event.getRegistry().register(user55);
            event.getRegistry().register(user56);
            event.getRegistry().register(user57);
            
            event.getRegistry().register(user61);
            event.getRegistry().register(user62);
            event.getRegistry().register(user63);
            event.getRegistry().register(user64);
            event.getRegistry().register(user65);
            event.getRegistry().register(user66);
            event.getRegistry().register(user67);
            
            event.getRegistry().register(user71);
            event.getRegistry().register(user72);
            event.getRegistry().register(user73);
            event.getRegistry().register(user74);
            event.getRegistry().register(user75);
            event.getRegistry().register(user76);
            event.getRegistry().register(user77);
            
            
            // register user createion block
            event.getRegistry().register(usercreate);
        }   

        @SubscribeEvent
        public static void onRegistryRegisterItem(final RegistryEvent.Register<Item> event) {
        	Item.Properties prop = new Item.Properties();
        	// register user block items
            event.getRegistry().register(new BlockItem(user11, prop).setRegistryName("user11"));
            event.getRegistry().register(new BlockItem(user12, prop).setRegistryName("user12"));
            event.getRegistry().register(new BlockItem(user13, prop).setRegistryName("user13"));
            event.getRegistry().register(new BlockItem(user14, prop).setRegistryName("user14"));
            event.getRegistry().register(new BlockItem(user15, prop).setRegistryName("user15"));
            event.getRegistry().register(new BlockItem(user16, prop).setRegistryName("user16"));
            event.getRegistry().register(new BlockItem(user17, prop).setRegistryName("user17"));
            
            event.getRegistry().register(new BlockItem(user21, prop).setRegistryName("user21"));
            event.getRegistry().register(new BlockItem(user22, prop).setRegistryName("user22"));
            event.getRegistry().register(new BlockItem(user23, prop).setRegistryName("user23"));
            event.getRegistry().register(new BlockItem(user24, prop).setRegistryName("user24"));
            event.getRegistry().register(new BlockItem(user25, prop).setRegistryName("user25"));
            event.getRegistry().register(new BlockItem(user26, prop).setRegistryName("user26"));
            event.getRegistry().register(new BlockItem(user27, prop).setRegistryName("user27"));
            
            event.getRegistry().register(new BlockItem(user31, prop).setRegistryName("user31"));
            event.getRegistry().register(new BlockItem(user32, prop).setRegistryName("user32"));
            event.getRegistry().register(new BlockItem(user33, prop).setRegistryName("user33"));
            event.getRegistry().register(new BlockItem(user34, prop).setRegistryName("user34"));
            event.getRegistry().register(new BlockItem(user35, prop).setRegistryName("user35"));
            event.getRegistry().register(new BlockItem(user36, prop).setRegistryName("user36"));
            event.getRegistry().register(new BlockItem(user37, prop).setRegistryName("user37"));
            
            event.getRegistry().register(new BlockItem(user41, prop).setRegistryName("user41"));
            event.getRegistry().register(new BlockItem(user42, prop).setRegistryName("user42"));
            event.getRegistry().register(new BlockItem(user43, prop).setRegistryName("user43"));
            event.getRegistry().register(new BlockItem(user44, prop).setRegistryName("user44"));
            event.getRegistry().register(new BlockItem(user45, prop).setRegistryName("user45"));
            event.getRegistry().register(new BlockItem(user46, prop).setRegistryName("user46"));
            event.getRegistry().register(new BlockItem(user47, prop).setRegistryName("user47"));
            
            event.getRegistry().register(new BlockItem(user51, prop).setRegistryName("user51"));
            event.getRegistry().register(new BlockItem(user52, prop).setRegistryName("user52"));
            event.getRegistry().register(new BlockItem(user53, prop).setRegistryName("user53"));
            event.getRegistry().register(new BlockItem(user54, prop).setRegistryName("user54"));
            event.getRegistry().register(new BlockItem(user55, prop).setRegistryName("user55"));
            event.getRegistry().register(new BlockItem(user56, prop).setRegistryName("user56"));
            event.getRegistry().register(new BlockItem(user57, prop).setRegistryName("user57"));
            
            event.getRegistry().register(new BlockItem(user61, prop).setRegistryName("user61"));
            event.getRegistry().register(new BlockItem(user62, prop).setRegistryName("user62"));
            event.getRegistry().register(new BlockItem(user63, prop).setRegistryName("user63"));
            event.getRegistry().register(new BlockItem(user64, prop).setRegistryName("user64"));
            event.getRegistry().register(new BlockItem(user65, prop).setRegistryName("user65"));
            event.getRegistry().register(new BlockItem(user66, prop).setRegistryName("user66"));
            event.getRegistry().register(new BlockItem(user67, prop).setRegistryName("user67"));
            
            event.getRegistry().register(new BlockItem(user71, prop).setRegistryName("user71"));
            event.getRegistry().register(new BlockItem(user72, prop).setRegistryName("user72"));
            event.getRegistry().register(new BlockItem(user73, prop).setRegistryName("user73"));
            event.getRegistry().register(new BlockItem(user74, prop).setRegistryName("user74"));
            event.getRegistry().register(new BlockItem(user75, prop).setRegistryName("user75"));
            event.getRegistry().register(new BlockItem(user76, prop).setRegistryName("user76"));
            event.getRegistry().register(new BlockItem(user77, prop).setRegistryName("user77"));
            
            // register user creation block item
            event.getRegistry().register(new BlockItem(usercreate, prop).setRegistryName("usercreate"));
            // register key to set and save password
            event.getRegistry().register(key);
            // register key to add character event
            event.getRegistry().register(charKey);
        }
    }
    
    
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber()
    public static class RegistryEvents { 	
        @SubscribeEvent
        public static void onStart(FMLServerStartingEvent startEvent) throws IOException {
        	thisEvent = startEvent;
        	// setting up all the custom commands /setPass /save /clr /login <username>, current player needs to be update after the user is logged in
        	cmd.setDisp(thisEvent.getCommandDispatcher());
        //	startEvent.getServer().setPlayerList(list);
        	//thisEvent.getServer().PlayerList().
//        	File dest = new File("./saves");
//        	Path dest2 = Paths.get("");
//      Files.copy(Password3D.class.getClassLoader().getResourceAsStream("assets/password3d/saves/Login World"), Minecraft.getInstance().getSaveLoader().func_215781_c(), StandardCopyOption.REPLACE_EXISTING);
        }
        
//		@SubscribeEvent
//		public static void onGUI(GuiOpenEvent event) throws IOException {
//			System.out.println(event.getGui() + "############################################");
//			if (event.getGui() instanceof IngameMenuScreen) {
//		         boolean flag = Minecraft.getInstance().isIntegratedServerRunning();
//		         boolean flag1 = Minecraft.getInstance().isConnectedToRealms();
//		         Minecraft.getInstance().world.sendQuittingDisconnectingPacket();
//		         if (flag) {
//		            Minecraft.getInstance().func_213231_b(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
//		         } else {
//		            Minecraft.getInstance().func_213254_o();
//		         }
//
//		         if (flag) {
//		            Minecraft.getInstance().displayGuiScreen(new MainMenuScreen());
//		         } else if (flag1) {
//		            RealmsBridge realmsbridge = new RealmsBridge();
//		            realmsbridge.switchToRealms(new MainMenuScreen());
//		         } else {
//		            Minecraft.getInstance().displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
//		         }
//		         event.setCanceled(true);
//
//			}
//			if (event.getGui() instanceof MainMenuScreen) {
////	        	File file = new File("./saves");
////				File source = new File(Password3D.class.getClassLoader().getResource("assets/password3d/saves/Login World").getPath());
////	        	try {
////	        	    FileUtils.copyDirectory(source, file);
////	        	} catch (IOException e) {
////	        	}
////	        	System.out.println(Password3D.class.getClassLoader().getResource("assets/password3d/saves/Login World").getPath());
//			Minecraft.getInstance().getSaveLoader().getFile("Login World", Password3D.class.getClassLoader().getResource("assets/password3d/saves/Login World").getPath());
//			}
//		}
        
    	/*
    	 * events that happen on player log in
    	 */
        @SubscribeEvent
        public static void onLogIn(PlayerLoggedInEvent playerLogEvent) throws AnvilConverterException, FileNotFoundException, InterruptedException, IOException {
        	player = playerLogEvent.getPlayer();
    		player.getServer().getCommandManager().handleCommand(player.getCommandSource(), "/tp 18 69 235");
    		// clear player inventory
    		player.inventory.clear();
        	// PlayerEntity converted to PasswordPlayer to store passwords
        	//update base player in commands and adding users
        	//passwordPlayer = new PasswordPlayer(player);
        	cmd.setPlayer(player);
        	key.setPlayer(player);
        	key.setCmd(cmd);
        	charKey.setPlayer(player);
        	charKey.setCmd(cmd);

//        	player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource(), "/worldborder center 20 217");
//        	player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource(), "/worldborder set 60");

        	KeyBinding.setKeyBindState(KeyBindings.esc.getKeyBinding().getKey(), true);
        }
        /*
         * event that happens on player logout the world is reconstructed and data relating 3D password is saved
         */
		@SubscribeEvent
		public static void onLogOut(PlayerLoggedOutEvent playerLogEvent)
				throws InterruptedException, FileNotFoundException, IOException {
        	player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource(), "/kill @e[type=minecraft:item]");
			World world = player.world;
			if (!world.isRemote()) {
				for (BlockPos pos : destBlocksPos) {
					world.destroyBlock(pos, false);
				}
				for (BlockPos pos : placedBlocksPos) {
					world.destroyBlock(pos, false);
				}

				for (int i = 0; i < destBlocksPos.size(); i++) {
					world.setBlockState(destBlocksPos.get(i), destBlocks.get(i));
				}
			}

			if (cmd.passwordPlayer != null && cmd.passwordPlayer.isPasswordLocked()) {
				cmd.passwordPlayer.updatePosition(player.getPosition());
				FileOutputStream file = new FileOutputStream(fileLoc);
				ObjectOutputStream object = new ObjectOutputStream(file);

				// Method for serialisation of object
				object.writeObject(cmd);

				object.close();
				file.close();
				// // saves the data about the player
			}
			// reads the date about the users from a file
			try {
				// Reading the object from a file
				FileInputStream file = new FileInputStream(fileLoc);
				ObjectInputStream object = new ObjectInputStream(file);

				cmd = (SetCommands) object.readObject();

				object.close();
				file.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (cmd == null) {
				cmd = new SetCommands();
				// cmd.setPlayers();
			}
			key.setSetPass(true);
			playerLoginStatus = false;
		}
		
    }
    
    public class RegistryEvents2 {
        /*
         * events that happen on player breaking the block
         */
        
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void blockDestroyed(BreakEvent playerDestEvent) {
        	   
			if (!(playerDestEvent.getState().getBlock() instanceof UserCreateBlock) && !(playerDestEvent.getState().getBlock() instanceof UserBlock)) {
				destBlocks.add(playerDestEvent.getState());
				destBlocksPos.add(playerDestEvent.getPos());
			}   
			blockState = playerDestEvent.getState();
        	   addAndCheckEvent("playerDestEvent", player.getPosition(), playerDestEvent.getPos(), player.getHeldItemMainhand().getItem().toString());   
}
           /*
            * events that happen when the player places the block
            */
           @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void blockPlaced(EntityPlaceEvent playerPlaceEvent) {
			if (!(playerPlaceEvent.getState().getBlock() instanceof UserCreateBlock) && !(playerPlaceEvent.getState().getBlock() instanceof UserBlock)) {
				placedBlocksPos.add(playerPlaceEvent.getPos());
			}
			blockState = playerPlaceEvent.getState();
			addAndCheckEvent("playerPlaceEvent", player.getPosition(), playerPlaceEvent.getPos(),
					player.getHeldItemMainhand().getItem().toString());
        }       
    }
    
    
    
    /*
     * add the event for the password if the password is set the password is checked
     */
    public static void addAndCheckEvent(String eventName, BlockPos playerPos,  BlockPos blockPos, String itemInHand) {
 	   if(cmd.passwordPlayer != null) {  
	        	if(player != null) {
	         		// add event to the password if password is not set	        	
	    			if(cmd.getpassBeenSet()) {
	        		cmd.passwordPlayer.getEvents().add(eventName);
	        		cmd.passwordPlayer.addEventLoc(playerPos);
	        		cmd.passwordPlayer.addBlockLoc(blockPos);
	        		cmd.passwordPlayer.getItemsInHand().add(itemInHand);
	        		Auth.addEventType(eventName, blockPos, blockState);
//	        		player.sendMessage(new StringTextComponent(cmd.passwordPlayer.getEvents().toString()+"Events ~~~"));
//	        		player.sendMessage(new StringTextComponent("added position and place to the array"));
	        		
	    			} else if(!cmd.getpassBeenSet() && cmd.passwordPlayer.getEvents().size() > 0) {
		    		    // lock array for the password
		    			if(!cmd.passwordPlayer.isPasswordLocked()) {
		    				checkEvent = new Auth(cmd.passwordPlayer.getEvents(),cmd.passwordPlayer.getEventsLoc(), cmd.passwordPlayer.getBlocksLoc(), cmd.passwordPlayer.getItemsInHand()); 
		    				cmd.passwordPlayer.setPasswordLocked(true);
		    			} 		   
		    			// check position and event
		    			else if(checkEvent.check(eventName, playerPos, blockPos, itemInHand)) {
		    		    	// initialise the password again
		    		    	if(checkEvent.eventsLeft() == 0) {
		    		    		player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource(), "/worldborder set 600000");
		    		    		player.sendMessage(new StringTextComponent("You successfully completed the pattern"));
		    		    		player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource(), "/tp Dev ");
		    		    		checkEvent = new Auth(cmd.passwordPlayer.getEvents(),cmd.passwordPlayer.getEventsLoc(), cmd.passwordPlayer.getBlocksLoc(), cmd.passwordPlayer.getItemsInHand());
		    		    		player.getServer().getCommandManager().handleCommand(player.getCommandSource(), "/tp "+cmd.passwordPlayer.getPos().getX()+" "+cmd.passwordPlayer.getPos().getY()+" "+ cmd.passwordPlayer.getPos().getZ());
		    		    		playerLoginStatus = true;    		    		
		    		    	} else {
		    		    		Auth.addEventType(eventName, blockPos, blockState);
		    		    	}
//		    		    	player.sendMessage(new StringTextComponent("event has been removed playerDestEvent"));
		    		    } else {
		    		    		checkEvent.addBadEvent();
		    		    		Auth.addEventType(eventName, blockPos, blockState);
		    		    }
	    		}
		     //   	System.out.println(player + " This is the player");
		     //   	player.sendMessage(new StringTextComponent("You have placed a block"));		
	        	}
 	   }
    }
    
    /*
     * give items to the player 
     */
    public static void giveItems() {
    	// pickaxes
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(521)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(535)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(539)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(543)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(550)));
    	
    	// showels
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(520)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(534)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(538)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(542)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(549)));
    	
    	// axes
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(522)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(536)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(540)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(544)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(551)));
    	
    	// hoes
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(555)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(556)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(557)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(558)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(559)));
    	
    	// swords
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(532)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(533)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(537)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(541)));
    	player.addItemStackToInventory(new ItemStack(Item.getItemById(548)));		
    }
}
