package class_scheduling3;

/**
 * 教授类
 * @author WZQ
 *
 */
public class Professor {
	private final int professorId;
	private final String professorName;

	public Professor(int professorId, String professorName) {
		this.professorId = professorId;
		this.professorName = professorName;
	}

	public int getProfessorId() {
		return this.professorId;
	}

	public String getProfessorName() {
		return this.professorName;
	}
}
