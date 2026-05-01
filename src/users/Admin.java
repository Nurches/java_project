package users;

import core.UniversitySystem;
import enums.UserRole;

public class Admin extends Employee {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String login, String password, String name, String email, double salary, String department) {
        super(id, login, password, name, email, UserRole.ADMIN, salary, department);
    }

    public void addUser(User user) {
        UniversitySystem.getInstance().addUser(user);
    }

    public void removeUser(User user) {
        UniversitySystem.getInstance().removeUser(user);
    }

    public void updateUser(User user) {
        // TODO Implement update strategy once persistence and validation layers are ready.
        UniversitySystem.getInstance().removeUser(user);
        UniversitySystem.getInstance().addUser(user);
    }
}
