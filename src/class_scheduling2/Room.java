package class_scheduling2;

public class Room {
	private final int roomId;
	private final String roomNumber;
	private final int roomCapacity; //教室容量

	public Room(int roomId, String roomNumber, int roomCapacity) {
		this.roomId = roomId;
		this.roomNumber = roomNumber;
		this.roomCapacity = roomCapacity;
	}

	public int getRoomId() {
		return roomId;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public int getRoomCapacity() {
		return roomCapacity;
	}
}
