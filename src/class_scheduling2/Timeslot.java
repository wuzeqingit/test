package class_scheduling2;

/**
 * 时段类
 * 	表示班级上课在周几的什么时间
 * @author WZQ
 *
 */
public class Timeslot {
	private final int timeslotId;
	private final String timeslot; //时间：如Mon 8:00 - 8:45

	public Timeslot(int timeslotId, String timeslot) {
		this.timeslotId = timeslotId;
		this.timeslot = timeslot;
	}

	public int getTimeslotId() {
		return this.timeslotId;
	}

	public String getTimeslot() {
		return this.timeslot;
	}
}
