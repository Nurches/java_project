package app.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import academic.Course;
import academic.CourseRegistration;
import academic.Mark;
import comparators.ResearchPaperCitationsComparator;
import comparators.ResearchPaperDateComparator;
import app.launch.AppLauncher;
import core.AuthService;
import core.DataStorage;
import core.UniversitySystem;
import enums.CourseType;
import enums.Language;
import enums.ManagerType;
import enums.TeacherTitle;
import exceptions.AuthenticationException;
import exceptions.RegistrationException;
import exceptions.UserNotFoundException;
import reports.ReportGenerator;
import research.ResearchJournal;
import research.ResearchPaper;
import users.Admin;
import users.BachelorStudent;
import users.Manager;
import users.MasterStudent;
import users.Teacher;

public final class DemoRunner {
    private DemoRunner() {
    }

    public static void run() {
        try {
            UniversitySystem system = UniversitySystem.getInstance();

            Admin admin = new Admin("A1", "admin", "admin123", "System Admin", "admin@uni.edu", 5000.0, "IT");
            BachelorStudent student = new BachelorStudent("S1", "student", "stud123", "Alice Student",
                    "alice@uni.edu", "Computer Science", 4);
            Teacher teacher = new Teacher("T1", "teacher", "teach123", "Dr. Brown", "brown@uni.edu", 4000.0,
                    "CS", TeacherTitle.PROFESSOR);
            MasterStudent master = new MasterStudent("M1", "master", "master123", "Bob Master", "bob@uni.edu",
                    "Data Science", 1);
            Manager manager = new Manager("MN1", "manager", "man123", "Mia Manager", "mia@uni.edu", 4200,
                    "Registrar", ManagerType.REGISTRAR);

            admin.addUser(admin);
            admin.addUser(student);
            admin.addUser(teacher);
            admin.addUser(master);
            admin.addUser(manager);

            Course course = new Course("C1", "OOP", 5, CourseType.MAJOR, Language.ENGLISH, "Computer Science", 4);
            system.addCourse(course);
            manager.assignTeacherToCourse(teacher, course);

            CourseRegistration registration = new CourseRegistration(student, course);
            system.addRegistration(registration);
            manager.approveRegistration(registration);
            if (registration.getStatus().name().equals("APPROVED")) {
                student.registerForCourse(course);
            }

            Mark mark = new Mark(28, 29, 35);
            teacher.putMark(student, course, mark);
            student.rateTeacher(teacher, 5);

            List<String> authors = new ArrayList<>();
            authors.add(master.getName());
            authors.add(teacher.getName());

            ResearchPaper paper1 = new ResearchPaper("AI in Education", authors, "Journal of AI", "Engineering", 12, 5,
                    "10.1000/xyz123", LocalDate.of(2025, 11, 20));
            ResearchPaper paper2 = new ResearchPaper("Adaptive Learning Systems", authors, "IEEE Transactions",
                    "Engineering", 20, 10, "10.1000/xyz124", LocalDate.of(2026, 3, 10));

            master.becomeResearcher();
            master.addResearchPaper(paper1);
            teacher.addResearchPaper(paper1);
            teacher.addResearchPaper(paper2);
            manager.addResearchPaper(paper2);

            system.addResearchPaper(paper1);
            system.addResearchPaper(paper2);

            System.out.println("Master student h-index: " + master.calculateHIndex());
            System.out.println("Teacher average rating: " + teacher.getAverageRating());

            ResearchJournal journal = system.getResearchJournal();
            journal.subscribe(student);
            journal.subscribe(teacher);
            journal.publishPaper(paper2);

            System.out.println("All papers sorted by citations:");
            system.printAllResearchPapers(new ResearchPaperCitationsComparator());

            System.out.println("All researcher papers sorted by date:");
            system.printAllResearchersPapers(new ResearchPaperDateComparator());

            system.getTopCitedResearcherBySchool("Engineering")
                    .ifPresent(r -> System.out.println(
                            "Top cited researcher in Engineering citations=" + r.getTotalCitations()));

            system.getTopCitedResearcherOfCurrentYear()
                    .ifPresent(r -> System.out.println(
                            "Top cited researcher of current year citations=" + r.getTotalCitations()));

            AuthService authService = new AuthService();
            authService.login("admin", "admin123");

            ReportGenerator reportGenerator = new ReportGenerator();
            System.out.println(reportGenerator.generateSystemReport());
            System.out.println(reportGenerator.generateMarksStatistics());
            System.out.println(reportGenerator.generateStudentReport(student));

            new DataStorage().save(system, AppLauncher.DEFAULT_DATA_FILE);

            System.out.println("Full demo completed successfully.");
        } catch (RegistrationException | AuthenticationException | UserNotFoundException e) {
            System.err.println("Business scenario failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
