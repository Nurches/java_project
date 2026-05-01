package users;

import enums.UserRole;
import exceptions.LowHIndexException;
import research.DiplomaProject;
import research.Researcher;

public class GraduateStudent extends Student {
    private static final long serialVersionUID = 1L;

    protected Researcher supervisor;
    protected DiplomaProject diplomaProject;

    public GraduateStudent(String id, String login, String password, String name, String email, String major,
            int yearOfStudy) {
        super(id, login, password, name, email, major, yearOfStudy);
        this.role = UserRole.STUDENT;
    }

    public void setSupervisor(Researcher supervisor) throws LowHIndexException {
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

    public DiplomaProject getDiplomaProject() {
        return diplomaProject;
    }

    public void setDiplomaProject(DiplomaProject diplomaProject) {
        this.diplomaProject = diplomaProject;
    }
}
