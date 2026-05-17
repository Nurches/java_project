package users;

import enums.UserRole;

public class PhDStudent extends GraduateStudent {
    private static final long serialVersionUID = 1L;

    public PhDStudent(String id, String login, String password, String name, String email, String major,
            int yearOfStudy) {
        super(id, login, password, name, email, major, yearOfStudy);
        this.role = UserRole.PHD_STUDENT;
    }
}
