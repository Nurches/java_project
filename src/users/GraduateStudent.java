package users;

import enums.UserRole;
import research.DiplomaProject;

public class GraduateStudent extends Student {
    private static final long serialVersionUID = 1L;

    protected DiplomaProject diplomaProject;

    public GraduateStudent(String id, String login, String password, String name, String email, String major,
            int yearOfStudy) {
        super(id, login, password, name, email, major, yearOfStudy);
        this.role = UserRole.STUDENT;
    }

    public DiplomaProject getDiplomaProject() {
        return diplomaProject;
    }

    public void setDiplomaProject(DiplomaProject diplomaProject) {
        this.diplomaProject = diplomaProject;
    }
}
