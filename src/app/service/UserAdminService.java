package app.service;

import app.audit.AuditLogger;
import app.repository.UserRepository;
import core.UniversitySystem;
import enums.ManagerType;
import enums.TeacherTitle;
import enums.UserRole;
import patterns.UserFactory;
import users.Admin;
import users.BachelorStudent;
import users.Student;
import users.Manager;
import users.MasterStudent;
import users.PhDStudent;
import users.Teacher;
import users.User;

public class UserAdminService {
    private final UserRepository userRepository;
    private final RbacService rbacService;
    private final AuditLogger auditLogger;
    private final UserFactory userFactory = new UserFactory();

    public UserAdminService(UserRepository userRepository, RbacService rbacService, AuditLogger auditLogger) {
        this.userRepository = userRepository;
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    public User createUser(User actor, UserRole role, String id, String login, String password, String name,
            String email, String major, int year, TeacherTitle teacherTitle, ManagerType managerType) {
        rbacService.requireRole(actor, UserRole.ADMIN);
        UniversitySystem system = UniversitySystem.getInstance();
        if (system.findUserByLogin(login) != null) {
            throw new IllegalArgumentException("Login already taken: " + login);
        }
        if (system.findUserById(id) != null) {
            throw new IllegalArgumentException("User id already taken: " + id);
        }
        User user = buildUser(role, id, login, password, name, email, major, year, teacherTitle, managerType);
        user.setPassword(password);
        userRepository.save(user);
        auditLogger.log(actor.getLogin(), "CREATE_USER", "users", "login=" + login + ",role=" + role);
        return user;
    }

    public void deleteUser(User actor, String login) {
        rbacService.requireRole(actor, UserRole.ADMIN);
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + login));
        if (user.getId().equals(actor.getId())) {
            throw new IllegalArgumentException("Cannot delete your own account while logged in");
        }
        if (user instanceof Admin) {
            long adminCount = userRepository.findAll().stream().filter(u -> u instanceof Admin).count();
            if (adminCount <= 1) {
                throw new IllegalArgumentException("Cannot delete the last admin account");
            }
        }
        userRepository.deleteById(user.getId());
        auditLogger.log(actor.getLogin(), "DELETE_USER", "users", "login=" + login);
    }

    public java.util.List<User> listUsers(User actor) {
        rbacService.requireRole(actor, UserRole.ADMIN, UserRole.MANAGER);
        return userRepository.findAll();
    }

    public void updateUser(User actor, String login, String name, String email, String newPassword, String major,
            int year) {
        rbacService.requireRole(actor, UserRole.ADMIN);
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + login));
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(newPassword);
        }
        if (user instanceof Student student) {
            if (major != null && !major.isBlank()) {
                student.setMajor(major);
            }
            if (year > 0) {
                student.setYearOfStudy(year);
            }
        }
        auditLogger.log(actor.getLogin(), "UPDATE_USER", "users", "login=" + login);
    }

    private User buildUser(UserRole role, String id, String login, String password, String name, String email,
            String major, int year, TeacherTitle teacherTitle, ManagerType managerType) {
        return switch (role) {
            case ADMIN -> new Admin(id, login, password, name, email, 5000.0, "IT");
            case STUDENT, BACHELOR_STUDENT -> new BachelorStudent(id, login, password, name, email, major, year);
            case MASTER_STUDENT -> new MasterStudent(id, login, password, name, email, major, year);
            case PHD_STUDENT -> new PhDStudent(id, login, password, name, email, major, year);
            case TEACHER -> teacherTitle != null
                    ? userFactory.createTeacherByTitle(teacherTitle, id, login, password, name, email, 4000.0, "CS")
                    : new Teacher(id, login, password, name, email, 4000.0, "CS", TeacherTitle.LECTURER);
            case MANAGER -> new Manager(id, login, password, name, email, 4200.0, "Office",
                    managerType != null ? managerType : ManagerType.REGISTRAR);
        };
    }
}
