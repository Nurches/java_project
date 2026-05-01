package patterns;

import enums.ManagerType;
import enums.TeacherTitle;
import enums.UserRole;
import users.Admin;
import users.BachelorStudent;
import users.Manager;
import users.MasterStudent;
import users.PhDStudent;
import users.Teacher;
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
                return new Teacher(id, login, password, name, email, 0.0, "General", TeacherTitle.LECTURER);
            case MANAGER:
                return new Manager(id, login, password, name, email, 0.0, "General", ManagerType.ACADEMIC);
            default:
                // TODO Extend factory for richer constructor arguments and validation profiles.
                throw new IllegalArgumentException("Unsupported role: " + role);
        }
    }
}
