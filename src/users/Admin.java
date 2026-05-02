package users;

import core.LogService;
import core.UniversitySystem;
import enums.UserRole;

public class Admin extends Employee {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String login, String password, String name, String email, double salary, String department) {
        super(id, login, password, name, email, UserRole.ADMIN, salary, department);
    }

    public void addUser(User user) {
        UniversitySystem.getInstance().addUser(user);
        new LogService().info("Admin added user: " + user.getLogin());
    }

    public void removeUser(User user) {
        UniversitySystem.getInstance().removeUser(user);
        new LogService().info("Admin removed user: " + user.getLogin());
    }

    public void updateUser(User oldUser, User updatedUser) {
        UniversitySystem.getInstance().removeUser(oldUser);
        UniversitySystem.getInstance().addUser(updatedUser);
        new LogService().info("Admin updated user: " + oldUser.getLogin());
    }
}
