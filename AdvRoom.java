/*
 * File: AdvRoom.java
 * ------------------
 * This file defines a class that models a single room in the
 * Adventure game.
 */

import java.io.*;
import java.util.*;

/* Class: AdvRoom */
/**
 * This class defines a single room in the Adventure game. A room is
 * characterized by the following properties:
 * 
 * <ul>
 * <li>A room number, which must be greater than zero
 * <li>Its name, which is a one-line string identifying the room
 * <li>Its description, which is a multiline array describing the room
 * <li>A list of objects contained in the room
 * <li>A flag indicating whether the room has been visited
 * <li>A motion table specifying the exits and where they lead </li>
 * 
 * The external format of the room data file is described in the assignment
 * handout. The comments on the methods exported by this class show how to use
 * the initialized data structure.
 */

public class AdvRoom {
	public void setDescription(String[] description) {
		this.description = description;
	}
	/* Method: getRoomNumber() */
	/**
	 * Returns the room number.
	 * 
	 * @usage int roomNumber = room.getRoomNumber();
	 * @return The room number
	 */
	public int getRoomNumber() {
		//		return super.getRoomNumber(); // Replace with your code
		return this.roomNumber;
	}

	/* Method: getName() */
	/**
	 * Returns the room name, which is its one-line description.
	 * 
	 * @usage String name = room.getName();
	 * @return The room name
	 */
	public String getName() {
		//		return super.getName(); // Replace with your code
		return this.name;
	}

	/* Method: getDescription() */
	/**
	 * Returns an array of strings that correspond to the long description of
	 * the room (including the list of the objects in the room).
	 * 
	 * @usage String[] description = room.getDescription();
	 * @return An array of strings giving the long description of the room
	 */
	public String[] getDescription() {
		//		return super.getDescription(); // Replace with your code
		if (beenVisited) {
			String[] shortDescription = new String[1];
			shortDescription[0] = this.name;
			return shortDescription;
		} else {
			return this.description;
		}
	}

	/* Method: addObject(obj) */
	/**
	 * Adds an object to the list of objects in the room.
	 * 
	 * @usage room.addObject(obj);
	 * @param The
	 *            AdvObject to be added
	 */
	public void addObject(AdvObject obj) {
		objects.add(obj);
		//		super.addObject(obj);
	}

	/* Method: removeObject(obj) */
	/**
	 * Removes an object from the list of objects in the room.
	 * 
	 * @usage room.removeObject(obj);
	 * @param The
	 *            AdvObject to be removed
	 */
	public void removeObject(AdvObject obj) {
		objects.remove(obj);
		//		super.removeObject(obj);
	}

	/* Method: containsObject(obj) */
	/**
	 * Checks whether the specified object is in the room.
	 * 
	 * @usage if (room.containsObject(obj)) . . .
	 * @param The
	 *            AdvObject being tested
	 * @return true if the object is in the room, and false otherwise
	 */
	public boolean containsObject(AdvObject obj) {
		//		return super.containsObject(obj);
		return objects.contains(obj);
	}

	/* Method: getObjectCount() */
	/**
	 * Returns the number of objects in the room.
	 * 
	 * @usage int nObjects = room.getObjectCount();
	 * @return The number of objects in the room
	 */
	public int getObjectCount() {
		//		return super.getObjectCount();
		return objects.size();
	}

	/* Method: getObject(index) */
	/**
	 * Returns the specified element from the list of objects in the room.
	 * 
	 * @usage AdvObject obj = room.getObject(index);
	 * @return The AdvObject at the specified index position
	 */
	public AdvObject getObject(int index) {
		//		return super.getObject(index);
		return objects.get(index);
	}

	/* Method: setVisited(flag) */
	/**
	 * Sets the flag indicating that this room has been visited according to the
	 * value of the parameter. Calling setVisited(true) means that the room has
	 * been visited; calling setVisited(false) restores its initial unvisited
	 * state.
	 * 
	 * @usage room.setVisited(flag);
	 * @param flag
	 *            The new state of the "visited" flag
	 */
	public void setVisited(boolean flag) {
		//		super.setVisited(flag); // Replace with your code
		this.beenVisited = flag;
	}

	/* Method: hasBeenVisited() */
	/**
	 * Returns true if the room has previously been visited.
	 * 
	 * @usage if (room.hasBeenVisited()) . . .
	 * @return true if the room has been visited; false otherwise
	 */
	public boolean hasBeenVisited() {
		//		return super.hasBeenVisited(); // Replace with your code
		return this.beenVisited;
	}

	/* Method: getMotionTable() */
	/**
	 * Returns the motion table associated with this room, which is an array of
	 * directions, room numbers, and enabling objects stored in a
	 * AdvMotionTableEntry.
	 * 
	 * @usage AdvMotionTableEntry[] motionTable = room.getMotionTable();
	 * @return The array of motion table entries associated with this room
	 */
	public AdvMotionTableEntry[] getMotionTable() {
		//		return super.getMotionTable(); // Replace with your code
		return this.motionTable;
	}

	/* Method: readFromFile(rd) */
	/**
	 * Reads the data for this room from the Scanner scan, which must have been
	 * opened by the caller. This method returns a room if the room
	 * initialization is successful; if there are no more rooms to read,
	 * readFromFile returns null.
	 * 
	 * @usage AdvRoom room = AdvRoom.readFromFile(scan);
	 * @param scan
	 *            A scanner open on the rooms data file
	 * @return a room if successfully read; null if at end of file
	 */
	public static AdvRoom readFromFile(Scanner scan) {
		AdvRoom room = new AdvRoom();
		String line = "";
		String desc = "";
		String keyName = null;
		int roomNum = 0;
		ArrayList<AdvMotionTableEntry> motionArrayList= new ArrayList<AdvMotionTableEntry>();
		room.roomNumber = Integer.parseInt(scan.nextLine());
		room.name = scan.nextLine();
		while ((line = scan.nextLine()).compareTo("-----") != 0) {
			desc += line + "\n";
			room.description = desc.split("\\n");
		}
		while (scan.hasNext() && !(line = scan.nextLine()).trim().equals("")) {
			String[] parts = line.split("\\s"); 
			String direction = parts[0].trim();
			boolean hasKey = parts[parts.length-1].contains("/");
			if(!hasKey) {
				roomNum = Integer.parseInt(parts[parts.length-1]);
			}
			if (hasKey) {
				String[] roomWithKey = parts[parts.length-1].split("/");
				roomNum = Integer.parseInt(roomWithKey[0]);
				keyName = roomWithKey[1];
			}
			room.motions.put(direction, roomNum);
			AdvMotionTableEntry motion = new AdvMotionTableEntry(direction, roomNum, keyName);
			RoomList roomList = new RoomList();
			roomList.setRoomNumber(roomNum);
			roomList.setNeedsKeys(hasKey);
			roomList.setKeyName(keyName);
			roomList.setCommand(direction);
			if(room.commandOntoMotion.containsKey(direction)) {
				ArrayList<RoomList> list = room.commandOntoMotion.get(direction);
				list.add(roomList);
				room.commandOntoMotion.put(direction, list);
			} else {
				ArrayList<RoomList> list = new ArrayList<RoomList>();
				list.add(roomList);
				room.commandOntoMotion.put(direction, list);
			}
			keyName = null;
			motionArrayList.add(motion);
		}
		room.motionTable = new AdvMotionTableEntry[motionArrayList.size()];
		room.motionTable = motionArrayList.toArray(room.motionTable);
		return room;
	}

	public Integer getDestRoom(String userInput) {
		return this.motions.get(userInput);
//		return AdvRoomStub.readFromFile(scan); // Replace with your code
	}

	@Override
	public String toString() {
		return "AdvRoom [roomNumber=" + roomNumber + ", name=" + name + ", description=" + Arrays.toString(description)
		+ ", object=" + object + ", beenVisited=" + beenVisited + ", motionTable="
		+ Arrays.toString(motionTable) + ", objects=" + objects + "]";
	}

	public ArrayList<AdvObject> getObjectList(){
		return objects;
	}


	private int roomNumber;
	private String name;
	protected String[] description;
	private AdvObject object;
	private boolean beenVisited;
	private AdvMotionTableEntry[] motionTable;
	private ArrayList<AdvObject> objects = new ArrayList<AdvObject>();
	private HashMap<String, Integer> motions = new HashMap<String, Integer>();
	protected HashMap<String, ArrayList<RoomList>> commandOntoMotion = new HashMap<String, ArrayList<RoomList>>();
	/* Private instance variables */
	// Add your own instance variables here

}
