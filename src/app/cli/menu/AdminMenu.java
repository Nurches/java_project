package app.cli.menu;

import java.util.List;

import app.cli.CliIO;
import app.cli.SessionContext;
import enums.CourseType;
import enums.Language;
import enums.ManagerType;
import enums.TeacherTitle;
import enums.UserRole;
import users.User;

public class AdminMenu {
    private final CliIO io;
    private final SessionContext session;

    public AdminMenu(CliIO io, SessionContext session) {
        this.io = io;
        this.session = session;
    }

    public void run() {
        boolean loop = true;
        while (loop) {
            int choice = io.chooseMenu("Admin", List.of(
                    "List users",
                    "Create user",
                    "Update user",
                    "Delete user",
                    "List courses",
                    "Create course",
                    "System report",
                    "Marks statistics",
                    "Audit log"));
            try {
                switch (choice) {
                    case 1 -> listUsers();
                    case 2 -> createUser();
                    case 3 -> updateUser();
                    case 4 -> deleteUser();
                    case 5 -> listCourses();
                    case 6 -> createCourse();
                    case 7 -> io.println(session.services().reportingService.systemReport(session.currentUser()));
                    case 8 -> io.println(session.services().reportingService.marksStats(session.currentUser()));
                    case 9 -> showAudit();
                    case 0 -> loop = false;
                    default -> {
                        if (choice != -1) {
                            io.println("Unknown option");
                        }
                    }
                }
            } catch (Exception e) {
                io.println("Error: " + e.getMessage());
            }
            if (choice != 0 && choice != -1) {
                io.pause();
            }
        }
    }

    private void listUsers() {
        var users = session.services().userAdminService.listUsers(session.currentUser());
        io.println("Total users: " + users.size() + " (no limit per role — add more via Create user)");
        users.forEach(u -> io.println(u.getLogin() + " | " + u.getRole() + " | " + u.getName()));
    }

    private void createUser() {
        String roleRaw = io.readLine("Role (ADMIN/MANAGER/TEACHER/STUDENT/MASTER_STUDENT/PHD_STUDENT): ").toUpperCase();
        UserRole role = UserRole.valueOf(roleRaw);
        String id = io.readLine("ID: ");
        String login = io.readLine("Login: ");
        String password = io.readLine("Password: ");
        String name = io.readLine("Name: ");
        String email = io.readLine("Email: ");
        String major = io.readLine("Major (students): ");
        int year = io.readInt("Year of study: ");
        TeacherTitle title = role == UserRole.TEACHER
                ? TeacherTitle.valueOf(io.readLine("Teacher title (TUTOR/LECTURER/SENIOR_LECTURER/PROFESSOR): ").toUpperCase())
                : null;
        ManagerType mt = role == UserRole.MANAGER
                ? ManagerType.valueOf(io.readLine("Manager type (REGISTRAR/ACADEMIC): ").toUpperCase())
                : null;
        User created = session.services().userAdminService.createUser(session.currentUser(), role, id, login,
                password, name, email, major, year, title, mt);
        io.println("Created: " + created.getLogin());
    }

    private void updateUser() {
        String login = io.readLine("Login to update: ");
        String name = io.readLine("New name (empty = keep): ");
        String email = io.readLine("New email (empty = keep): ");
        String password = io.readLine("New password (empty = keep): ");
        String major = io.readLine("Major for students (empty = keep): ");
        int year = io.readInt("Year for students (0 = keep): ");
        session.services().userAdminService.updateUser(session.currentUser(), login, name, email, password, major,
                year);
        io.println("User updated.");
    }

    private void deleteUser() {
        String login = io.readLine("Login to delete: ");
        session.services().userAdminService.deleteUser(session.currentUser(), login);
        io.println("Deleted.");
    }

    private void listCourses() {
        session.services().courseAdminService.listCourses(session.currentUser())
                .forEach(c -> io.println(c.toString()));
    }

    private void createCourse() {
        String id = io.readLine("Course ID: ");
        String name = io.readLine("Name: ");
        int credits = io.readInt("Credits: ");
        CourseType type = CourseType.valueOf(io.readLine("Type (MAJOR/ELECTIVE/GENERAL): ").toUpperCase());
        Language lang = Language.valueOf(io.readLine("Language (ENGLISH/KAZAKH/RUSSIAN): ").toUpperCase());
        String major = io.readLine("Intended major: ");
        int year = io.readInt("Intended year: ");
        session.services().courseAdminService.createCourse(session.currentUser(), id, name, credits, type, lang,
                major, year);
        io.println("Course created.");
    }

    private void showAudit() {
        session.services().auditLogger.recent(20).forEach(e -> io.println(e.toString()));
    }
}
