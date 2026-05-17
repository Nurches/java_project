package users;

import enums.UserRole;
import exceptions.LowHIndexException;
import research.Researcher;

/**
 * Represents a bachelor student.
 * A 4th-year bachelor student may be assigned a research supervisor.
 */
public class BachelorStudent extends Student {
    private static final long serialVersionUID = 1L;

    private Researcher supervisor;

    public BachelorStudent(String id, String login, String password, String name, String email, String major,
            int yearOfStudy) {
        super(id, login, password, name, email, major, yearOfStudy);
        this.role = UserRole.BACHELOR_STUDENT;
    }

    /**
     * Assigns a supervisor to a 4th-year bachelor student.
     *
     * @param supervisor the researcher who will supervise the student
     * @throws LowHIndexException if supervisor h-index is less than 3
     * @throws IllegalStateException if the student is not on the 4th year
     */
    public void setSupervisor(Researcher supervisor) throws LowHIndexException {
        if (yearOfStudy != 4) {
            throw new IllegalStateException("Supervisor is only for 4th year bachelor students");
        }
        if (supervisor == null) {
            this.supervisor = null;
            return;
        }
        if (supervisor.calculateHIndex() < 3) {
            throw new LowHIndexException("Supervisor h-index must be at least 3");
        }
        this.supervisor = supervisor;
    }

    public Researcher getSupervisor() {
        return supervisor;
    }
}
