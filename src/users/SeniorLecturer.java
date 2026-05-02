package users;

import enums.TeacherTitle;

public class SeniorLecturer extends Teacher {
    private static final long serialVersionUID = 1L;

    public SeniorLecturer(String id, String login, String password, String name, String email, double salary,
            String department) {
        super(id, login, password, name, email, salary, department, TeacherTitle.SENIOR_LECTURER);
    }
}
