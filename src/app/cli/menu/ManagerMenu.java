package app.cli.menu;

import java.util.List;

import app.cli.CliIO;
import app.cli.SessionContext;
import academic.CourseRegistration;
import communication.News;
import core.UniversitySystem;
import enums.NewsTopic;
import exceptions.RegistrationException;
import enums.RegistrationStatus;
import enums.UrgencyLevel;
import research.ResearchPaper;
import research.Researcher;
import users.Manager;
import users.Student;
import users.Teacher;

public class ManagerMenu {
    private final CliIO io;
    private final SessionContext session;

    public ManagerMenu(CliIO io, SessionContext session) {
        this.io = io;
        this.session = session;
    }

    public void run() {
        boolean loop = true;
        Manager manager = (Manager) session.currentUser();
        while (loop) {
            int choice = io.chooseMenu("Manager", List.of(
                    "Pending registrations",
                    "Approve registration",
                    "Reject registration",
                    "Assign teacher to course",
                    "List courses",
                    "Students by GPA",
                    "Students alphabetically",
                    "Teachers by rating",
                    "Publish news",
                    "View complaints",
                    "Research: top by school",
                    "Research: top this year",
                    "Add research paper",
                    "Publish paper to journal",
                    "All papers (by citations)",
                    "Create research project",
                    "System report",
                    "Marks statistics"));
            try {
                switch (choice) {
                    case 1 -> listRegistrations(RegistrationStatus.PENDING);
                    case 2 -> approveRegistration();
                    case 3 -> rejectRegistration();
                    case 4 -> assignTeacher();
                    case 5 -> session.services().courseAdminService.listCourses(session.currentUser())
                            .forEach(c -> io.println(c.toString()));
                    case 6 -> manager.viewStudentsSortedByGpa().forEach(s -> io.println(s.getName() + " GPA=" + s.getGpa()));
                    case 7 -> manager.viewStudentsSortedAlphabetically()
                            .forEach(s -> io.println(s.getName() + " (" + s.getLogin() + ")"));
                    case 8 -> manager.viewTeachersSortedByRating()
                            .forEach(t -> io.println(t.getName() + " rating=" + t.getAverageRating()));
                    case 9 -> publishNews();
                    case 10 -> listComplaints();
                    case 11 -> topBySchool();
                    case 12 -> topYear();
                    case 13 -> addPaper(manager);
                    case 14 -> publishJournal();
                    case 15 -> session.services().researchService.printAllPapers("citations");
                    case 16 -> {
                        String topic = io.readLine("Project topic: ");
                        session.services().researchService.createProject(session.currentUser(), topic);
                        io.println("Project created.");
                    }
                    case 17 -> io.println(session.services().reportingService.systemReport(session.currentUser()));
                    case 18 -> io.println(session.services().reportingService.marksStats(session.currentUser()));
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

    private void listRegistrations(RegistrationStatus status) {
        session.services().registrationService.listByStatus(status).forEach(r -> io.println(formatReg(r)));
    }

    private void approveRegistration() throws RegistrationException {
        CourseRegistration reg = pickRegistration(RegistrationStatus.PENDING);
        if (reg == null) {
            return;
        }
        session.services().registrationService.approve(session.currentUser(), reg);
        io.println("Approved.");
    }

    private void rejectRegistration() {
        CourseRegistration reg = pickRegistration(RegistrationStatus.PENDING);
        if (reg == null) {
            return;
        }
        String reason = io.readLine("Rejection reason: ");
        session.services().registrationService.reject(session.currentUser(), reg, reason);
        io.println("Rejected.");
    }

    private CourseRegistration pickRegistration(RegistrationStatus status) {
        List<CourseRegistration> list = session.services().registrationService.listByStatus(status);
        return io.chooseFromList("Registrations", list, ManagerMenu::formatReg);
    }

    private static String formatReg(CourseRegistration r) {
        return r.getStudent().getLogin() + " -> " + r.getCourse().getId() + " [" + r.getStatus() + "]";
    }

    private void assignTeacher() {
        String teacherLogin = io.readLine("Teacher login: ");
        String courseId = io.readLine("Course ID: ");
        session.services().courseAdminService.assignTeacher(session.currentUser(), teacherLogin, courseId);
        io.println("Assigned.");
    }

    private void publishNews() {
        String title = io.readLine("Title: ");
        String content = io.readLine("Content: ");
        NewsTopic topic = NewsTopic.valueOf(io.readLine("Topic (ACADEMIC/RESEARCH/EVENTS/ADMINISTRATIVE): ").toUpperCase());
        session.services().newsService.publish(session.currentUser(), new News(title, content, topic));
        io.println("Published.");
    }

    private void listComplaints() {
        session.services().complaintService.listAll(session.currentUser())
                .forEach(c -> io.println(c.getSender().getName() + " -> " + c.getTargetStudent().getLogin()
                        + " [" + c.getUrgencyLevel() + "] " + c.getText()));
    }

    private void topBySchool() {
        String school = io.readLine("School: ");
        session.services().researchService.topBySchool(school)
                .ifPresentOrElse(r -> io.println("Top: citations=" + r.getTotalCitations()),
                        () -> io.println("No researcher found."));
    }

    private void topYear() {
        session.services().researchService.topCurrentYear()
                .ifPresentOrElse(r -> io.println("Top this year: citations=" + r.getTotalCitations()),
                        () -> io.println("No researcher found."));
    }

    private void addPaper(Researcher researcher) {
        ResearchPaper paper = ResearchMenuHelper.createPaper(io, session, researcher);
        if (paper != null) {
            io.println("Paper added. h-index=" + researcher.calculateHIndex());
        }
    }

    private void publishJournal() {
        List<ResearchPaper> papers = UniversitySystem.getInstance().getResearchPapers();
        ResearchPaper paper = io.chooseFromList("Papers", papers, ResearchPaper::getTitle);
        if (paper != null) {
            session.services().researchService.publishToJournal(session.currentUser(), paper);
            io.println("Published to journal.");
        }
    }
}
