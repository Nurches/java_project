package patterns;

import enums.ManagerType;
import enums.TeacherTitle;
import enums.UserRole;
import users.Admin;
import users.BachelorStudent;
import users.Manager;
import users.MasterStudent;
import users.PhDStudent;
import users.Professor;
import users.SeniorLecturer;
import users.Teacher;
import users.Tutor;
import users.User;

public class UserFactory {

    public User createUser(UserRole role, String id, String login, String password, String name, String email) {
        switch (role) {
            case ADMIN:
                return new Admin(id, login, password, name, email, 0.0, "Administration");
            case BACHELOR_STUDENT:
            case STUDENT:
                return new BachelorStudent(id, login, password, name, email, "UNDECLARED", 1);
            case MASTER_STUDENT:
                return new MasterStudent(id, login, password, name, email, "UNDECLARED", 1);
            case PHD_STUDENT:
                return new PhDStudent(id, login, password, name, email, "UNDECLARED", 1);
            case TEACHER:
                return new Tutor(id, login, password, name, email, 0.0, "General");
            case MANAGER:
                return new Manager(id, login, password, name, email, 0.0, "General", ManagerType.ACADEMIC);
            default:
                throw new IllegalArgumentException("Unsupported role: " + role);
        }
    }

    public Teacher createTeacherByTitle(TeacherTitle title, String id, String login, String password, String name,
            String email, double salary, String department) {
        switch (title) {
            case TUTOR:
                return new Tutor(id, login, password, name, email, salary, department);
            case SENIOR_LECTURER:
                return new SeniorLecturer(id, login, password, name, email, salary, department);
            case PROFESSOR:
                return new Professor(id, login, password, name, email, salary, department);
            case LECTURER:
            default:
                return new Teacher(id, login, password, name, email, salary, department, TeacherTitle.LECTURER);
        }
    }
}
