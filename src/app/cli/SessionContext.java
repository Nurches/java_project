package app.cli;

import app.service.AppServices;
import users.User;

public class SessionContext {
    private final AppServices services;
    private User currentUser;

    public SessionContext(AppServices services) {
        this.services = services;
    }

    public AppServices services() {
        return services;
    }

    public User currentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
