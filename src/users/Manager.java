package users;

import java.util.ArrayList;
import java.util.Comparator;
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
import research.ResearchPaper;
import research.Researcher;

public class Manager extends Employee implements Researcher {
    private static final long serialVersionUID = 1L;

    private ManagerType managerType;
    private List<ResearchPaper> researchPapers;

    public Manager(String id, String login, String password, String name, String email, double salary, String department,
            ManagerType managerType) {
        super(id, login, password, name, email, UserRole.MANAGER, salary, department);
        this.managerType = managerType;
        this.researchPapers = new ArrayList<>();
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public void approveRegistration(CourseRegistration registration) throws RegistrationException {
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

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(researchPapers);
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        if (!researchPapers.contains(paper)) {
            researchPapers.add(paper);
        }
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(comparator);
        sorted.forEach(System.out::println);
    }

    @Override
    public int calculateHIndex() {
        List<Integer> citations = researchPapers.stream()
                .map(ResearchPaper::getCitations)
                .sorted(Comparator.reverseOrder())
                .toList();

        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }

    @Override
    public int getTotalCitations() {
        return researchPapers.stream().mapToInt(ResearchPaper::getCitations).sum();
    }
}
