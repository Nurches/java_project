package app.service;

import enums.UserRole;
import users.User;

public class RbacService {
    public void requireRole(User user, UserRole... roles) {
        for (UserRole role : roles) {
            if (user.getRole() == role) {
                return;
            }
        }
        throw new SecurityException("Access denied for role: " + user.getRole());
    }
}
