package users;

import enums.UserRole;

public class MasterStudent extends GraduateStudent {
    private static final long serialVersionUID = 1L;

    public MasterStudent(String id, String login, String password, String name, String email, String major,
            int yearOfStudy) {
        super(id, login, password, name, email, major, yearOfStudy);
        this.role = UserRole.MASTER_STUDENT;
    }
}
