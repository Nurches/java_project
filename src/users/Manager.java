package users;

import java.util.List;

import academic.Course;
import academic.CourseRegistration;
import communication.News;
import comparators.StudentAlphabetComparator;
import comparators.StudentGpaComparator;
import comparators.TeacherRatingComparator;
import enums.ManagerType;
import enums.UserRole;
import exceptions.RegistrationException;
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

    public void approveRegistration(CourseRegistration registration) throws RegistrationException {
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
        registration.approve();
    }

    public void rejectRegistration(CourseRegistration registration, String reason) {
        registration.reject(reason);
    }

    public void assignTeacherToCourse(Teacher teacher, Course course) {
        teacher.assignCourse(course);
    }

    public String generateReport() {
        return new ReportGenerator().generateSystemReport();
    }

    public String generateMarksStatistics() {
        return new ReportGenerator().generateMarksStatistics();
    }

    public void publishNews(News news) {
        core.UniversitySystem.getInstance().addNews(news);
    }

    public List<Student> viewStudentsSortedByGpa() {
        List<Student> students = core.UniversitySystem.getInstance().getStudents();
        students.sort(new StudentGpaComparator());
        return students;
    }

    public List<Student> viewStudentsSortedAlphabetically() {
        List<Student> students = core.UniversitySystem.getInstance().getStudents();
        students.sort(new StudentAlphabetComparator());
        return students;
    }

    public List<Teacher> viewTeachersSortedByRating() {
        List<Teacher> teachers = core.UniversitySystem.getInstance().getTeachers();
        teachers.sort(new TeacherRatingComparator());
        return teachers;
    }
}
