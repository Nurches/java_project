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

/**
 * Handles course registration requests and approval workflow.
 */
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

    /**
     * Creates a registration request for a student and target course.
     *
     * @param actor the user performing the action
     * @param student the student for whom the request is created
     * @param courseId the target course id
     * @return created registration request
     */
    public CourseRegistration createRequest(User actor, Student student, String courseId) {
        rbacService.requireRole(actor, UserRole.STUDENT, UserRole.BACHELOR_STUDENT, UserRole.MASTER_STUDENT,
                UserRole.PHD_STUDENT, UserRole.MANAGER, UserRole.ADMIN);
        if (isStudentActor(actor) && !actor.getId().equals(student.getId())) {
            throw new SecurityException("Students can create registration requests only for themselves");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        CourseRegistration registration = new CourseRegistration(student, course);
        registrationRepository.save(registration);
        auditLogger.log(actor.getLogin(), "CREATE_REGISTRATION", "course_registrations",
                "student=" + student.getLogin() + ",courseId=" + courseId);
        return registration;
    }

    /**
     * Approves a registration and performs actual enrollment if business checks pass.
     *
     * @param actor the approving user
     * @param registration the registration to approve
     * @throws RegistrationException if approval or enrollment fails
     */
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

    /**
     * Rejects a registration request with a textual reason.
     *
     * @param actor the rejecting user
     * @param registration the registration to reject
     * @param reason rejection explanation
     */
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
            String reason = String.format(
                    "Eligibility mismatch: student major='%s', year=%d; course requires major='%s', year=%s",
                    student.getMajor(),
                    student.getYearOfStudy(),
                    course.getIntendedMajor(),
                    course.getIntendedYearOfStudy() <= 0 ? "any" : String.valueOf(course.getIntendedYearOfStudy()));
            registration.reject(reason);
            throw new RegistrationException("Registration rejected: " + reason);
        }
        if (!student.canRegisterForMoreCourses()) {
            registration.reject("Student has reached failed courses limit");
            throw new RegistrationException("Registration rejected: failed courses limit reached");
        }
        List<String> missingPrerequisites = course.getMissingPrerequisites(student);
        if (!missingPrerequisites.isEmpty()) {
            String reason = "Missing prerequisite courses: " + String.join(", ", missingPrerequisites);
            registration.reject(reason);
            throw new RegistrationException("Registration rejected: " + reason);
        }
        if (!course.hasCapacity()) {
            String reason = "Course capacity reached";
            registration.reject(reason);
            throw new RegistrationException("Registration rejected: " + reason);
        }
        registration.approve();
    }

    private boolean isStudentActor(User actor) {
        return switch (actor.getRole()) {
            case STUDENT, BACHELOR_STUDENT, MASTER_STUDENT, PHD_STUDENT -> true;
            default -> false;
        };
    }
}
