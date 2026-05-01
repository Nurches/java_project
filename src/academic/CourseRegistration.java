package academic;

import java.io.Serializable;

import enums.RegistrationStatus;
import users.Student;

public class CourseRegistration implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private Course course;
    private RegistrationStatus status;

    public CourseRegistration(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.status = RegistrationStatus.PENDING;
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

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
}
