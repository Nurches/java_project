package app.cli;

import java.util.NoSuchElementException;

import app.cli.menu.AdminMenu;
import app.cli.menu.CommonMenu;
import app.cli.menu.ManagerMenu;
import app.cli.menu.StudentMenu;
import app.cli.menu.TeacherMenu;
import app.service.AppServices;
import enums.UserRole;
import exceptions.AuthenticationException;
import exceptions.UserNotFoundException;
import users.User;

public class ConsoleApp {
    private final CliIO io = new CliIO();
    private final AppServices services = new AppServices();
    private final SessionContext session = new SessionContext(services);

    public void run() {
        io.println("University System CLI");
        io.println("Type 'exit' at login to quit. Data is saved on exit.");

        while (true) {
            User user = loginFlow();
            if (user == null) {
                io.println("Goodbye.");
                return;
            }
            session.setCurrentUser(user);
            roleLoop();
        }
    }

    private User loginFlow() {
        while (true) {
            try {
                String login = io.readLine("Login (or 'exit'): ");
                if (login.isBlank()) {
                    continue;
                }
                if ("exit".equalsIgnoreCase(login)) {
                    return null;
                }
                String password = io.readLine("Password: ");
                User user = services.authService.login(login, password);
                io.println("Welcome, " + user.getName() + " (" + user.getRole() + ")");
                return user;
            } catch (AuthenticationException | UserNotFoundException e) {
                io.println("Auth failed: " + e.getMessage());
            } catch (NoSuchElementException e) {
                return null;
            }
        }
    }

    private void roleLoop() {
        boolean loop = true;
        while (loop) {
            int choice = io.chooseMenu("Main menu (" + session.currentUser().getRole() + ")", buildMainOptions());
            try {
                switch (choice) {
                    case 1 -> new CommonMenu(io, session).run();
                    case 2 -> runRoleMenu();
                    case 3 -> loop = false;
                    default -> {
                        if (choice != -1) {
                            io.println("Unknown option");
                        }
                    }
                }
            } catch (Exception e) {
                io.println("Error: " + e.getMessage());
            }
        }
        session.setCurrentUser(null);
    }

    private java.util.List<String> buildMainOptions() {
        return java.util.List.of(
                "Common (profile, news, messages)",
                roleLabel() + " actions",
                "Logout");
    }

    private String roleLabel() {
        return switch (session.currentUser().getRole()) {
            case ADMIN -> "Admin";
            case MANAGER -> "Manager";
            case TEACHER -> "Teacher";
            default -> "Student";
        };
    }

    private void runRoleMenu() {
        UserRole role = session.currentUser().getRole();
        switch (role) {
            case ADMIN -> new AdminMenu(io, session).run();
            case MANAGER -> new ManagerMenu(io, session).run();
            case TEACHER -> new TeacherMenu(io, session).run();
            case STUDENT, BACHELOR_STUDENT, MASTER_STUDENT, PHD_STUDENT -> new StudentMenu(io, session).run();
            default -> io.println("No role menu for " + role);
        }
    }
}

