package class_scheduling;

/**
 * 学生分组类 类似于一个班级
 * 
 * @author WZQ
 *
 */
public class Group {
	private final int groupId;
	private final int groupSize; 
	private final int moduleIds[];

	public Group(int groupId, int groupSize, int moduleIds[]) {
		this.groupId = groupId;
		this.groupSize = groupSize;
		this.moduleIds = moduleIds;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public int getGroupSize() {
		return this.groupSize;
	}

	public int[] getModuleIds() {
		return this.moduleIds;
	}
}
