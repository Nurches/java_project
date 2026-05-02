package reports;

import java.util.DoubleSummaryStatistics;

import core.UniversitySystem;
import users.Student;

public class ReportGenerator {

    public String generateSystemReport() {
        UniversitySystem system = UniversitySystem.getInstance();
        return "System report: users=" + system.getUsers().size() +
                ", students=" + system.getStudents().size() +
                ", teachers=" + system.getTeachers().size() +
                ", courses=" + system.getCourses().size() +
                ", papers=" + system.getResearchPapers().size() +
                ", messages=" + system.getMessages().size() +
                ", news=" + system.getNews().size();
    }

    public String generateMarksStatistics() {
        UniversitySystem system = UniversitySystem.getInstance();
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

    public String generateStudentReport(Student student) {
        return "Student report: " + student.getName() +
                ", gpa=" + String.format("%.2f", student.getGpa()) +
                ", credits=" + student.getCredits() +
                ", failedCourses=" + student.getFailedCoursesCount();
    }
}
