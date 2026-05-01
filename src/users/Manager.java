package users;

import academic.Course;
import academic.CourseRegistration;
import enums.ManagerType;
import enums.RegistrationStatus;
import enums.UserRole;
import reports.ReportGenerator;

public class Manager extends Employee {
    private static final long serialVersionUID = 1L;

    private ManagerType managerType;

    public Manager(String id, String login, String password, String name, String email, double salary, String department,
            ManagerType managerType) {
        super(id, login, password, name, email, UserRole.MANAGER, salary, department);
        this.managerType = managerType;
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public void approveRegistration(CourseRegistration registration) {
        registration.setStatus(RegistrationStatus.APPROVED);
    }

    public void assignTeacherToCourse(Teacher teacher, Course course) {
        teacher.assignCourse(course);
    }

    public String generateReport() {
        return new ReportGenerator().generateSystemReport();
    }
}
