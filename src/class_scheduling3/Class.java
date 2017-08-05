package class_scheduling3;

/**
 * 表示一个学生分组在特定的时间，特定的教室，跟随特定的教授，学习一门课程的一部分
 * @author zeQin
 *
 */
public class Class {
	private final int classId;
	private final int groupId; //上课的学生
	private final int moduleId; //课程的信息
	private int professorId; //教授
	private int timeslotId; //时间
	private int roomId; //课室

	public Class(int classId, int groupId, int moduleId) {
		this.classId = classId;
		this.moduleId = moduleId;
		this.groupId = groupId;
	}

	public void addProfessor(int professorId) {
		this.professorId = professorId;
	}

	public void addTimeslot(int timeslotId) {
		this.timeslotId = timeslotId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getClassId() {
		return this.classId;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public int getModuleId() {
		return this.moduleId;
	}

	public int getProfessorId() {
		return this.professorId;
	}

	public int getTimeslotId() {
		return this.timeslotId;
	}

	public int getRoomId() {
		return this.roomId;
	}
}
