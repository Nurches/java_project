package app.cli.menu;

import java.util.List;

import app.cli.CliIO;
import app.cli.SessionContext;
import academic.Course;
import academic.Mark;
import enums.UrgencyLevel;
import research.Researcher;
import users.Student;
import users.Teacher;

public class TeacherMenu {
    private final CliIO io;
    private final SessionContext session;

    public TeacherMenu(CliIO io, SessionContext session) {
        this.io = io;
        this.session = session;
    }

    public void run() {
        boolean loop = true;
        Teacher teacher = (Teacher) session.currentUser();
        while (loop) {
            int choice = io.chooseMenu("Teacher", List.of(
                    "My courses",
                    "Put mark",
                    "File complaint",
                    "Student report",
                    "Marks statistics",
                    "Become researcher",
                    "Leave researcher role",
                    "Research menu",
                    "My rating"));
            try {
                switch (choice) {
                    case 1 -> teacher.getCourses().forEach(c -> io.println(c.getId() + " " + c.getName()));
                    case 2 -> putMark(teacher);
                    case 3 -> fileComplaint();
                    case 4 -> studentReport();
                    case 5 -> io.println(session.services().reportingService.marksStats(session.currentUser()));
                    case 6 -> {
                        teacher.becomeResearcher();
                        io.println("You are now a researcher.");
                    }
                    case 7 -> {
                        teacher.leaveResearcherRole();
                        io.println("Researcher role updated.");
                    }
                    case 8 -> {
                        if (teacher.isResearcherActive()) {
                            ResearchMenuHelper.researchSubmenu(io, session, teacher);
                        } else {
                            io.println("Become a researcher first.");
                        }
                    }
                    case 9 -> io.println("Average rating: " + teacher.getAverageRating());
                    case 0 -> loop = false;
                    default -> {
                        if (choice != -1) {
                            io.println("Unknown option");
                        }
                    }
                }
            } catch (Exception e) {
                io.println("Error: " + e.getMessage());
            }
            if (choice != 0 && choice != -1) {
                io.pause();
            }
        }
    }

    private void putMark(Teacher teacher) {
        List<Course> courses = teacher.getCourses();
        Course course = io.chooseFromList("Your courses", courses, c -> c.getId() + " " + c.getName());
        if (course == null) {
            return;
        }
        String studentLogin = io.readLine("Student login: ");
        Student student = (Student) core.UniversitySystem.getInstance().findUserByLogin(studentLogin);
        if (student == null) {
            io.println("Student not found.");
            return;
        }
        double a1 = io.readDouble("Attestation 1: ");
        double a2 = io.readDouble("Attestation 2: ");
        double fin = io.readDouble("Final: ");
        session.services().gradingService.putMark(session.currentUser(), teacher, student, course,
                new Mark(a1, a2, fin));
        io.println("Mark saved.");
    }

    private void fileComplaint() {
        String studentLogin = io.readLine("Student login: ");
        String text = io.readLine("Complaint text: ");
        UrgencyLevel urgency = UrgencyLevel.valueOf(io.readLine("Urgency (LOW/MEDIUM/HIGH/CRITICAL): ").toUpperCase());
        session.services().complaintService.file(session.currentUser(), studentLogin, text, urgency);
        io.println("Complaint filed.");
    }

    private void studentReport() {
        String login = io.readLine("Student login: ");
        Student student = (Student) core.UniversitySystem.getInstance().findUserByLogin(login);
        if (student == null) {
            io.println("Not found.");
            return;
        }
        io.println(session.services().reportingService.studentReport(session.currentUser(), student));
    }
}
