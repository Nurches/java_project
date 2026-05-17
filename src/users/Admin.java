package users;

import core.LogService;
import core.UniversitySystem;
import enums.UserRole;

/**
 * Represents an administrator responsible for system-level user management.
 */
public class Admin extends Employee {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String login, String password, String name, String email, double salary, String department) {
        super(id, login, password, name, email, UserRole.ADMIN, salary, department);
    }

    /**
     * Adds a user to the university system.
     *
     * @param user user to add
     */
    public void addUser(User user) {
        UniversitySystem.getInstance().addUser(user);
        new LogService().info("Admin added user: " + user.getLogin());
    }

    /**
     * Removes a user from the university system.
     *
     * @param user user to remove
     */
    public void removeUser(User user) {
        UniversitySystem.getInstance().removeUser(user);
        new LogService().info("Admin removed user: " + user.getLogin());
    }

    /**
     * Replaces an existing user object with an updated one.
     *
     * @param oldUser existing user to replace
     * @param updatedUser updated user data
     */
    public void updateUser(User oldUser, User updatedUser) {
        UniversitySystem.getInstance().removeUser(oldUser);
        UniversitySystem.getInstance().addUser(updatedUser);
        new LogService().info("Admin updated user: " + oldUser.getLogin());
    }
}
