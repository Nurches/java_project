package academic;

import java.io.Serializable;
import java.time.LocalDateTime;

import enums.RegistrationStatus;
import users.Student;

public class CourseRegistration implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private Course course;
    private RegistrationStatus status;
    private String rejectionReason;
    private LocalDateTime requestedAt;
    private LocalDateTime resolvedAt;

    public CourseRegistration(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.status = RegistrationStatus.PENDING;
        this.requestedAt = LocalDateTime.now();
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void approve() {
        this.status = RegistrationStatus.APPROVED;
        this.rejectionReason = null;
        this.resolvedAt = LocalDateTime.now();
    }

    public void reject(String reason) {
        this.status = RegistrationStatus.REJECTED;
        this.rejectionReason = reason;
        this.resolvedAt = LocalDateTime.now();
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
        this.resolvedAt = LocalDateTime.now();
    }
}
