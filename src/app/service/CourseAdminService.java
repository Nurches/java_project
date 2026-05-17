package app.service;

import java.util.ArrayList;
import java.util.List;

import academic.Course;
import app.audit.AuditLogger;
import app.repository.CourseRepository;
import core.UniversitySystem;
import enums.CourseType;
import enums.Language;
import enums.UserRole;
import users.Manager;
import users.Teacher;
import users.User;

public class CourseAdminService {
    private final CourseRepository courseRepository;
    private final RbacService rbacService;
    private final AuditLogger auditLogger;

    public CourseAdminService(CourseRepository courseRepository, RbacService rbacService, AuditLogger auditLogger) {
        this.courseRepository = courseRepository;
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    public Course createCourse(User actor, String id, String name, int credits, CourseType type, Language language,
            String major, int year) {
        return createCourse(actor, id, name, credits, type, language, major, year, Course.DEFAULT_MAX_STUDENTS, "");
    }

    public Course createCourse(User actor, String id, String name, int credits, CourseType type, Language language,
            String major, int year, int maxStudents, String prerequisitesRaw) {
        rbacService.requireRole(actor, UserRole.ADMIN, UserRole.MANAGER);
        Course course = new Course(id, name, credits, type, language, major, year, maxStudents,
                parsePrerequisites(prerequisitesRaw));
        courseRepository.save(course);
        auditLogger.log(actor.getLogin(), "CREATE_COURSE", "courses", "id=" + id);
        return course;
    }

    public List<Course> listCourses(User actor) {
        rbacService.requireRole(actor, UserRole.ADMIN, UserRole.MANAGER, UserRole.TEACHER, UserRole.STUDENT,
                UserRole.BACHELOR_STUDENT, UserRole.MASTER_STUDENT, UserRole.PHD_STUDENT);
        return courseRepository.findAll();
    }

    public void assignTeacher(User actor, String teacherLogin, String courseId) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        User found = UniversitySystem.getInstance().findUserByLogin(teacherLogin);
        if (!(found instanceof Teacher teacher)) {
            throw new IllegalArgumentException("Teacher not found");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        if (actor instanceof Manager manager) {
            manager.assignTeacherToCourse(teacher, course);
        } else {
            teacher.assignCourse(course);
        }
        auditLogger.log(actor.getLogin(), "ASSIGN_TEACHER", "courses",
                "teacher=" + teacherLogin + ",course=" + courseId);
    }

    private List<String> parsePrerequisites(String prerequisitesRaw) {
        List<String> prerequisiteIds = new ArrayList<>();
        if (prerequisitesRaw == null || prerequisitesRaw.isBlank()) {
            return prerequisiteIds;
        }
        for (String value : prerequisitesRaw.split(",")) {
            String trimmed = value.trim();
            if (!trimmed.isEmpty()) {
                prerequisiteIds.add(trimmed);
            }
        }
        return prerequisiteIds;
    }
}
