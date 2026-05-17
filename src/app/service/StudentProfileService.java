package app.service;

import java.util.List;

import app.audit.AuditLogger;
import app.repository.CourseRepository;
import academic.Course;
import enums.UserRole;
import exceptions.LowHIndexException;
import research.DiplomaProject;
import research.Researcher;
import users.BachelorStudent;
import users.GraduateStudent;
import users.Student;
import users.Teacher;
import users.User;

/**
 * Handles student-specific profile actions such as researcher mode, supervisor
 * assignment, diploma project setup, ratings, and course teacher lookup.
 */
public class StudentProfileService {
    private final CourseRepository courseRepository;
    private final RbacService rbacService;
    private final AuditLogger auditLogger;

    public StudentProfileService(CourseRepository courseRepository, RbacService rbacService,
            AuditLogger auditLogger) {
        this.courseRepository = courseRepository;
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    public void becomeResearcher(User actor) {
        requireSelfStudent(actor);
        ((Student) actor).becomeResearcher();
        auditLogger.log(actor.getLogin(), "BECOME_RESEARCHER", "users", "");
    }

    public void leaveResearcher(User actor) {
        requireSelfStudent(actor);
        ((Student) actor).leaveResearcherRole();
        auditLogger.log(actor.getLogin(), "LEAVE_RESEARCHER", "users", "");
    }

    public void rateTeacher(User actor, String teacherLogin, int rating) {
        requireSelfStudent(actor);
        Student student = (Student) actor;
        User target = core.UniversitySystem.getInstance().findUserByLogin(teacherLogin);
        if (!(target instanceof Teacher teacher)) {
            throw new IllegalArgumentException("Teacher not found");
        }
        student.rateTeacher(teacher, rating);
        auditLogger.log(actor.getLogin(), "RATE_TEACHER", "teachers", teacherLogin + "=" + rating);
    }

    /**
     * Assigns or clears a supervisor for a bachelor student.
     *
     * @param actor the student performing the action
     * @param supervisorLogin login of the supervisor to assign, or blank to clear
     * @throws LowHIndexException if supervisor h-index is below the required threshold
     */
    public void setSupervisor(User actor, String supervisorLogin) throws LowHIndexException {
        if (!(actor instanceof BachelorStudent bachelor)) {
            throw new IllegalStateException("Only bachelor students can set a supervisor");
        }
        requireSelfStudent(actor);
        if (supervisorLogin == null || supervisorLogin.isBlank()) {
            bachelor.setSupervisor(null);
            return;
        }
        User sup = core.UniversitySystem.getInstance().findUserByLogin(supervisorLogin);
        if (!(sup instanceof Researcher researcher)) {
            throw new IllegalArgumentException("Supervisor must be an active researcher");
        }
        if (!researcher.isResearcherActive()) {
            throw new IllegalArgumentException("Supervisor must be an active researcher");
        }
        bachelor.setSupervisor(researcher);
        auditLogger.log(actor.getLogin(), "SET_SUPERVISOR", "users", supervisorLogin);
    }

    /**
     * Creates or replaces a diploma project for a graduate student.
     *
     * @param actor the student performing the action
     * @param title diploma project title
     * @param description diploma project description
     */
    public void setDiplomaProject(User actor, String title, String description) {
        if (!(actor instanceof GraduateStudent grad)) {
            throw new IllegalStateException("Only graduate students can set a diploma project");
        }
        requireSelfStudent(actor);
        grad.setDiplomaProject(new DiplomaProject(title, description));
        auditLogger.log(actor.getLogin(), "SET_DIPLOMA", "users", title);
    }

    public List<Teacher> teachersForCourse(User actor, String courseId) {
        requireSelfStudent(actor);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        return course.getInstructors();
    }

    private void requireSelfStudent(User actor) {
        rbacService.requireRole(actor, UserRole.STUDENT, UserRole.BACHELOR_STUDENT, UserRole.MASTER_STUDENT,
                UserRole.PHD_STUDENT);
    }
}
