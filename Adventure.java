/*
 * File: Adventure.java
 * --------------------
 * This program plays the Adventure game from Assignment #4.
 */

import java.io.*;
import java.util.*;

/* Class: Adventure */
/**
 * This class is the main program class for the Adventure game.
 */

public class Adventure {

	// Use this scanner for any console input
	private static Scanner scan = new Scanner(System.in);


	/**
	 * This method is used only to test the program
	 */
	public static void setScanner(Scanner theScanner) {
		scan = theScanner;
		// Delete the following line when done
		//			AdventureStub.setScanner(theScanner);

	}

	/**
	 * Runs the adventure program
	 */

	public static void main(String[] args) {
		Adventure game = new Adventure();

		FileUtils fileUtil = new FileUtils(scan);
		try {
			Scanner roomsScanner = fileUtil.getScanner("Rooms");
			while (roomsScanner.hasNextInt()) {
				AdvRoom room = AdvRoom.readFromFile(roomsScanner);
				game.roomsMap.put(room.getRoomNumber(), room);
			}
			Scanner objectsScanner = fileUtil.getScanner("Objects");
			while (objectsScanner.hasNext()) {
				AdvObject object = AdvObject.readFromFile(objectsScanner);
				game.roomsMap.get(object.getInitialLocation()).addObject(object);
			}
			Scanner synonymsScanner = fileUtil.getScanner("Synonyms");
			String line;
			while (synonymsScanner.hasNextLine() && (line = synonymsScanner.nextLine().trim()).length() > 0) {
				String[] parts = line.split("=");
				game.synonymsMap.put(parts[0], parts[1]);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game.run(game);
		//		AdventureStub.main(args);
	}
	public void run(Adventure game) {
		Map.Entry<Integer, AdvRoom> entry = roomsMap.entrySet().iterator().next();
		currentRoom = entry.getValue();
		System.out.println(Arrays.toString(currentRoom.getDescription()));
		currentRoom.setVisited(true);
		while (true) {
			System.out.print("> ");
			command = scan.nextLine().trim().toUpperCase();
			String[] parts = command.split("\\s+");
			if (synonymsMap.containsKey(parts[0])) {
				command = synonymsMap.get(parts[0]).trim();
				parts[0] = command;
			}
			if (parts.length > 0) {
				switch (parts[0]) {
				case "TAKE":
					String originalItem = parts[1];
					if(originalItem == null || originalItem.isEmpty()) {
						System.out.println("Object is not here! Check your input!");
					} else {
						if(game.synonymsMap.containsKey(originalItem)) {
							String mappedItem = game.synonymsMap.get(originalItem);
							AdvObject selectedObj = this.getObj(currentRoom.getObjectList(), mappedItem);
							game.executeTakeCommand(selectedObj);
						} else {
							AdvObject selectedObj = this.getObj(currentRoom.getObjectList(), originalItem);
							if(selectedObj == null) {
								System.out.println("Object is not here! Check your input!");
							} else {
								game.executeTakeCommand(selectedObj);
							}
						}
					}
					break;
				case "DROP":
					String dropObject = parts[1];
					if(game.synonymsMap.containsKey(dropObject)) {
						String fullName = game.synonymsMap.get(dropObject).trim();
						AdvObject selectedObj = this.getObj(playerInventory, fullName);

						if(selectedObj == null) {
							System.out.println("You can't drop that! Check your inventory!");
						} else {
							game.executeDropCommand(selectedObj);
						}
					} else {
						AdvObject selectedObj = this.getObj(playerInventory, dropObject);
						if(selectedObj != null) {
							game.executeDropCommand(selectedObj);
						} else {
							System.out.println("You can't drop that! Check your inventory!");
						}
					}

					break;
				case "LOOK":
					game.executeLookCommand();
					break;
				case "QUIT":
					game.executeQuitCommand();
					break;
				case "INVENTORY":
					game.executeInventoryCommand();
					break;
				case "HELP":
					game.executeHelpCommand();
					break;
				default: 
					game.executeMotionCommand(command);
					break;
				}
			}
		}
	}

	/**
	 * Gets the lists of items in player inventory
	 * @param playerInventory
	 * @return
	 */
	public List<String> getInventory(List<AdvObject> playerInventory) {
		List<String> playerInventoryNames = new ArrayList<String>();
		for (int i = 0; i<playerInventory.size(); i++) {
			playerInventoryNames.add(playerInventory.get(i).getName());
		}
		return playerInventoryNames;
	}
	/**
	 * Executes a motion command. This method is called from the
	 * AdvMotionCommand class to move to a new room.
	 * 
	 * @param direction
	 *            The string indicating the direction of motion
	 */
	public void executeMotionCommand(String command) {
		//		super.executeMotionCommand(direction); // Replace with your code
		if(!command.isEmpty()) {
			AdvMotionTableEntry[] motionTable = currentRoom.getMotionTable();
			boolean needsKey = false;
			String key = null;
			for(int i = 0; i < motionTable.length; i++) {
				AdvMotionTableEntry motion = motionTable[i];
				if(motion.getDirection().equalsIgnoreCase(command)) {
					key = motion.getKeyName();
					if (key!=null) needsKey = true;
					break;
				}
			}
			if(!needsKey) {
				ArrayList<RoomList> current = currentRoom.commandOntoMotion.get(command);
				if(current == null) {
					System.out.println("Please enter in a valid command: ");
					System.out.println(Arrays.toString(currentRoom.getMotionTable()));
					return;
				}
				int roomNumber = current.get(0).roomNumber;
				currentRoom = roomsMap.get(roomNumber);
				while (hasForced(currentRoom)) {
					RoomList update = getForcedRoom(currentRoom);
					if(update != null) {
						currentRoom = roomsMap.get(update.getRoomNumber());
					} else {
						break;
					}
				}
				if(currentRoom != null) {
					System.out.println(Arrays.toString(currentRoom.getDescription()));
					if (currentRoom.getObjectCount() != 0) {
						for (AdvObject obj: currentRoom.getObjectList()) {
							System.out.println("There is " + obj.getName() + " here.");
						}
					}
					currentRoom.setVisited(true);
				} else {
					System.out.println("");
				}
			} else {
				AdvObject keyObject = this.getObj(playerInventory, key);
				if (playerInventory.contains(keyObject)) {
					int roomNumber = -1;
					for(RoomList room : currentRoom.commandOntoMotion.get(command)) {
						if(room.isNeedsKeys()) {
							roomNumber = room.getRoomNumber();
							break;
						}
					}
					currentRoom = roomsMap.get(roomNumber);
					while (hasForced(currentRoom)) {
						RoomList update = getForcedRoom(currentRoom);
						if(update != null) {
							currentRoom = roomsMap.get(update.getRoomNumber());

						} else {
							break;
						}
					}
					if(currentRoom != null) {
						System.out.println(Arrays.toString(currentRoom.getDescription()));
						if (currentRoom.getObjectCount() != 0) {
							for (AdvObject obj: currentRoom.getObjectList()) {
								System.out.println("There is " + obj.getName() + " here.");
							}
						}
						currentRoom.setVisited(true);
					} else {
						System.out.println("");
					}
				} else {
					int roomNumber = -1;
					for(RoomList room : currentRoom.commandOntoMotion.get(command)) {
						if(!room.isNeedsKeys()) {
							roomNumber = room.getRoomNumber();
							break;
						}
					}
					currentRoom = roomsMap.get(roomNumber);
					while (hasForced(currentRoom)) {
						RoomList update = getForcedRoom(currentRoom);
						if(update != null) {
							currentRoom = roomsMap.get(update.getRoomNumber());

						}else {
							break;
						}
					}
					if(currentRoom != null) {
						System.out.println(Arrays.toString(currentRoom.getDescription()));
						if (currentRoom.getObjectCount() != 0) {
							for (AdvObject obj: currentRoom.getObjectList()) {
								System.out.println("There is " + obj.getName() + " here.");
							}
						}
						currentRoom.setVisited(true);
					} else {
						System.out.println("");
					}

				}
			}
		}
	}
	
	/**
	 * This is a helper method for motion command.
	 * @param currentRoom
	 * @return
	 */
	public boolean hasForced (AdvRoom currentRoom) {
		String direction = "FORCED"; 
		for(AdvMotionTableEntry entry: currentRoom.getMotionTable()) {
			if(entry.getDirection().equals(direction)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This is a helper method for motion command.
	 * @param currentRoom
	 * @return
	 */
	public RoomList getForcedRoom(AdvRoom currentRoom) {
		for(AdvMotionTableEntry entry: currentRoom.getMotionTable()) {
			String direction = "FORCED"; 
			if (entry.getDirection().equals(direction)) {
				if(entry.getKeyName()!=null) {
					AdvObject keyObj = this.getObj(playerInventory, entry.getKeyName());
					if(playerInventory.contains(keyObj)) {
						int roomNumber = entry.getDestinationRoom();
						RoomList roomList = new RoomList();
						if (roomNumber != 0) {
							roomList.setRoomNumber(roomNumber);
							roomList.setNeedsKeys(entry.getKeyName() != null);
							roomList.setKeyName(entry.getKeyName());
							roomList.setCommand(direction);
							return roomList;
						} else {
							return null;
						}
					}

				} else {
					int roomNumber = entry.getDestinationRoom();
					RoomList roomList = new RoomList();
					if (roomNumber != 0) {
						roomList.setRoomNumber(roomNumber);
						roomList.setCommand(direction);
						return roomList;
					} else {
						return null;
					}
				}

			}
		}
		return null;


	}

	/* Method: executeQuitCommand() */
	/**
	 * Implements the QUIT command. This command should ask the user to confirm
	 * the quit request and, if so, should exit from the play method. If not,
	 * the program should continue as usual.
	 */
	public void executeQuitCommand() {
		//		super.executeQuitCommand(); // Replace with your code
		System.out.println("Are you sure you want to quit? (Y or N) > ");
		String answer = scan.next();
		if(answer.equalsIgnoreCase("Y")) {
			System.exit(0);
		} else {
			System.out.println("You choose to continue with the game.");
		}
	}
	/* Method: executeHelpCommand() */
	/**
	 * Implements the HELP command. Your code must include some help text for
	 * the user.
	 */
	public void executeHelpCommand() {
		//		super.executeHelpCommand(); // Replace with your code
		System.out.println("Here are your available destinations:");
		System.out.println(Arrays.toString(currentRoom.getMotionTable()));
	}

	/* Method: executeLookCommand() */
	/**
	 * Implements the LOOK command. This method should give the full description
	 * of the room and its contents.
	 */
	public void executeLookCommand() {
		System.out.println(Arrays.toString(currentRoom.description));
		if (currentRoom.getObjectCount() != 0) {
			for (AdvObject obj: currentRoom.getObjectList()) {
				System.out.println("There is " + obj.getName() + " here.");
			}
		}
		//		super.executeLookCommand(); // Replace with your code
	}

	/* Method: executeInventoryCommand() */
	/**
	 * Implements the INVENTORY command. This method should display a list of
	 * what the user is carrying.
	 */
	public void executeInventoryCommand() {
		//		super.executeInventoryCommand(); // Replace with your code
		if (getInventory(playerInventory).size() == 0) {
			System.out.println("You don't have anything!");
		} else {
			System.out.println("Here is your inventory:");
			System.out.println(Arrays.toString(getInventory(playerInventory).toArray()));
		}
	}

	/**
	 * Return and AdvObject based on user input/object name
	 */
	public AdvObject getObj(ArrayList<AdvObject> objects, String userInput) {
		for (AdvObject obj: objects) {
			if (obj.getName().equalsIgnoreCase(userInput)) {
				return obj;
			}
		}
		return null;
	}

	/* Method: executeTakeCommand(obj) */
	/**
	 * Implements the TAKE command. This method should check that the object is
	 * in the room and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to take
	 */

	public void executeTakeCommand(AdvObject obj) {
		//		super.executeTakeCommand(obj); // Replace with your code

		if (currentRoom.containsObject(obj)) {
			currentRoom.removeObject(obj);
			playerInventory.add(obj);
			System.out.println("You have taken the " + obj.getName() + ".");
		} else {
			System.out.println("The object isn't here!");
		}
	}

	/* Method: executeDropCommand(obj) */
	/**
	 * Implements the DROP command. This method should check that the user is
	 * carrying the object and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to drop
	 */
	public void executeDropCommand(AdvObject obj) {
		//		super.executeDropCommand(obj); // Replace with your code
		if (playerInventory.contains(obj)) {
			currentRoom.addObject(obj);
			playerInventory.remove(obj);
			System.out.println("You have dropped the " + obj.getName() + ".");
		} else {
			System.out.println("You cannot drop what you don't have!");
		}
	}

	/* Private instance variables */
	// Add your own instance variables here
	private HashMap<String, String> synonymsMap = new HashMap<String, String>();
	private HashMap<Integer, AdvRoom> roomsMap = new HashMap<Integer, AdvRoom>();
	private ArrayList<AdvObject> playerInventory = new ArrayList<AdvObject>();
	private AdvRoom currentRoom;
	private String command;
}
