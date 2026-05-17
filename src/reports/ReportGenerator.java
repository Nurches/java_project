package reports;

import java.util.DoubleSummaryStatistics;

import core.UniversitySystem;
import users.Student;

/**
 * Generates aggregate reports about the university and student performance.
 */
public class ReportGenerator {
    private final UniversitySystem system;

    /**
     * Creates a report generator using the shared university system instance.
     */
    public ReportGenerator() {
        this(UniversitySystem.getInstance());
    }

    /**
     * Creates a report generator for the provided system instance.
     *
     * @param system university system used as a report source
     */
    public ReportGenerator(UniversitySystem system) {
        this.system = system;
    }

    /**
     * Builds a high-level report about the current system state.
     *
     * @return system report text
     */
    public String generateSystemReport() {
        return "System report: users=" + system.getUsers().size() +
                ", students=" + system.getStudents().size() +
                ", teachers=" + system.getTeachers().size() +
                ", courses=" + system.getCourses().size() +
                ", papers=" + system.getResearchPapers().size() +
                ", messages=" + system.getMessages().size() +
                ", news=" + system.getNews().size();
    }

    /**
     * Builds aggregated statistics for all student marks stored in the system.
     *
     * @return marks statistics summary
     */
    public String generateMarksStatistics() {
        DoubleSummaryStatistics stats = system.getStudents().stream()
                .flatMap(student -> student.viewMarks().stream())
                .mapToDouble(mark -> mark.getTotal())
                .summaryStatistics();

        if (stats.getCount() == 0) {
            return "Marks statistics: no marks available";
        }

        long highPerformers = system.getStudents().stream()
                .filter(student -> student.getGpa() >= 3.0)
                .count();

        long atRiskStudents = system.getStudents().stream()
                .filter(student -> student.getFailedCoursesCount() > 0)
                .count();

        return "Marks statistics: count=" + stats.getCount() +
                ", min=" + stats.getMin() +
                ", max=" + stats.getMax() +
                ", avg=" + String.format("%.2f", stats.getAverage()) +
                ", highPerformers=" + highPerformers +
                ", atRiskStudents=" + atRiskStudents;
    }

    /**
     * Builds a compact report for a single student.
     *
     * @param student target student
     * @return student report text
     */
    public String generateStudentReport(Student student) {
        return "Student report: " + student.getName() +
                ", gpa=" + String.format("%.2f", student.getGpa()) +
                ", credits=" + student.getCredits() +
                ", failedCourses=" + student.getFailedCoursesCount();
    }
}
