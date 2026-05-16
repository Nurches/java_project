package app.service;

import academic.Course;
import academic.CourseRegistration;
import app.audit.AuditLogger;
import app.repository.CourseRepository;
import app.repository.RegistrationRepository;
import enums.RegistrationStatus;
import enums.UserRole;
import exceptions.RegistrationException;
import users.Manager;
import users.Student;
import users.User;

import java.util.List;

public class RegistrationService {
    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;
    private final RbacService rbacService;
    private final AuditLogger auditLogger;

    public RegistrationService(CourseRepository courseRepository, RegistrationRepository registrationRepository,
            RbacService rbacService, AuditLogger auditLogger) {
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    public CourseRegistration createRequest(User actor, Student student, String courseId) {
        rbacService.requireRole(actor, UserRole.STUDENT, UserRole.MANAGER, UserRole.ADMIN);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        CourseRegistration registration = new CourseRegistration(student, course);
        registrationRepository.save(registration);
        auditLogger.log(actor.getLogin(), "CREATE_REGISTRATION", "course_registrations",
                "student=" + student.getLogin() + ",courseId=" + courseId);
        return registration;
    }

    public void approve(User actor, CourseRegistration registration) throws RegistrationException {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        if (actor instanceof Manager manager) {
            manager.approveRegistration(registration);
        } else if (actor.getRole() == UserRole.ADMIN) {
            approveAsAdmin(registration);
        } else {
            throw new SecurityException("Only Manager or Admin can approve");
        }
        if (registration.getStatus().name().equals("APPROVED")) {
            try {
                registration.getStudent().registerForCourse(registration.getCourse());
            } catch (Exception e) {
                throw new RegistrationException("Approval failed during enrollment: " + e.getMessage());
            }
        }
        auditLogger.log(actor.getLogin(), "APPROVE_REGISTRATION", "course_registrations",
                "student=" + registration.getStudent().getLogin() + ",course=" + registration.getCourse().getId());
    }

    public void reject(User actor, CourseRegistration registration, String reason) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        if (actor instanceof Manager manager) {
            manager.rejectRegistration(registration, reason);
        } else {
            registration.reject(reason);
        }
        auditLogger.log(actor.getLogin(), "REJECT_REGISTRATION", "course_registrations",
                "student=" + registration.getStudent().getLogin() + ",reason=" + reason);
    }

    public List<CourseRegistration> listByStatus(RegistrationStatus status) {
        return registrationRepository.findByStatus(status);
    }

    public List<CourseRegistration> listAll() {
        return registrationRepository.findAll();
    }

    private void approveAsAdmin(CourseRegistration registration) throws RegistrationException {
        Student student = registration.getStudent();
        Course course = registration.getCourse();
        if (!course.isEligible(student)) {
            registration.reject("Student major/year does not match course eligibility");
            throw new RegistrationException("Registration rejected: eligibility mismatch");
        }
        if (!student.canRegisterForMoreCourses()) {
            registration.reject("Student has reached failed courses limit");
            throw new RegistrationException("Registration rejected: failed courses limit reached");
        }
        registration.approve();
    }
}
