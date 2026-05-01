import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import academic.Course;
import academic.Mark;
import comparators.ResearchPaperCitationsComparator;
import core.DataStorage;
import core.UniversitySystem;
import enums.CourseType;
import enums.Language;
import enums.TeacherTitle;
import users.Admin;
import users.BachelorStudent;
import users.MasterStudent;
import users.Teacher;
import research.ResearchJournal;
import research.ResearchPaper;

public class Main {
    public static void main(String[] args) {
        try {
            UniversitySystem system = UniversitySystem.getInstance();

            Admin admin = new Admin("A1", "admin", "admin123", "System Admin", "admin@uni.edu", 5000.0,
                    "IT");
            BachelorStudent student = new BachelorStudent("S1", "student", "stud123", "Alice Student",
                    "alice@uni.edu", "Computer Science", 2);
            Teacher teacher = new Teacher("T1", "teacher", "teach123", "Dr. Brown", "brown@uni.edu", 4000.0,
                    "CS", TeacherTitle.PROFESSOR);
            MasterStudent master = new MasterStudent("M1", "master", "master123", "Bob Master", "bob@uni.edu",
                    "Data Science", 1);

            admin.addUser(admin);
            admin.addUser(student);
            admin.addUser(teacher);
            admin.addUser(master);

            Course course = new Course("C1", "OOP", 5, CourseType.MAJOR, Language.ENGLISH);
            system.addCourse(course);

            teacher.assignCourse(course);
            student.registerForCourse(course);

            Mark mark = new Mark(28, 29, 35);
            teacher.putMark(student, course, mark);

            List<String> authors = new ArrayList<>();
            authors.add(master.getName());
            authors.add(teacher.getName());
            ResearchPaper paper = new ResearchPaper("AI in Education", authors, "Journal of AI", 12, 5,
                    "10.1000/xyz123", LocalDate.of(2025, 11, 20));

            master.addResearchPaper(paper);
            system.addResearchPaper(paper);

            System.out.println("Master student h-index: " + master.calculateHIndex());

            ResearchJournal journal = new ResearchJournal("Advanced Research Journal");
            journal.subscribe(student);
            journal.subscribe(teacher);
            journal.publishPaper(paper);

            system.printAllResearchPapers(new ResearchPaperCitationsComparator());

            DataStorage storage = new DataStorage();
            storage.save(system, "university_system.ser");

            System.out.println("Project skeleton demo completed successfully.");
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
