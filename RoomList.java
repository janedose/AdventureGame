
public class RoomList {
	private String command;
	private boolean needsKeys;
	private String keyName;
	int roomNumber;
	
	public RoomList (){
		
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isNeedsKeys() {
		return needsKeys;
	}

	public void setNeedsKeys(boolean needsKeys) {
		this.needsKeys = needsKeys;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Override
	public String toString() {
		return "RoomList [command=" + command + ", needsKeys=" + needsKeys + ", keyName=" + keyName + ", roomNumber="
				+ roomNumber + "]";
	}
	
}
