package users;

import enums.UserRole;
import exceptions.LowHIndexException;
import research.Researcher;

public class BachelorStudent extends Student {
    private static final long serialVersionUID = 1L;

    private Researcher supervisor;

    public BachelorStudent(String id, String login, String password, String name, String email, String major,
            int yearOfStudy) {
        super(id, login, password, name, email, major, yearOfStudy);
        this.role = UserRole.BACHELOR_STUDENT;
    }

    /**
     * Per requirements: only 4th-year bachelor students may have a supervisor-researcher.
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
