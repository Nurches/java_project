package users;

import enums.UserRole;

public class BachelorStudent extends Student {
    private static final long serialVersionUID = 1L;

    public BachelorStudent(String id, String login, String password, String name, String email, String major,
            int yearOfStudy) {
        super(id, login, password, name, email, major, yearOfStudy);
        this.role = UserRole.BACHELOR_STUDENT;
    }
}
