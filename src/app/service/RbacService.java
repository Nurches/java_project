package app.service;

import enums.UserRole;
import users.Student;
import users.User;

public class RbacService {
    public void requireRole(User user, UserRole... roles) {
        for (UserRole role : roles) {
            if (matchesRole(user, role)) {
                return;
            }
        }
        throw new SecurityException("Access denied for role: " + user.getRole());
    }

    private boolean matchesRole(User user, UserRole role) {
        if (user.getRole() == role) {
            return true;
        }
        if (role == UserRole.STUDENT && user instanceof Student) {
            return true;
        }
        return switch (role) {
            case BACHELOR_STUDENT, MASTER_STUDENT, PHD_STUDENT -> user instanceof Student;
            default -> false;
        };
    }
}
