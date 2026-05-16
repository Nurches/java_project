package app.cli.menu;

import java.util.List;

import app.cli.CliIO;
import app.cli.SessionContext;
import academic.Course;
import academic.CourseRegistration;
import academic.Mark;
import exceptions.LowHIndexException;
import users.GraduateStudent;
import users.MasterStudent;
import users.PhDStudent;
import users.Student;

public class StudentMenu {
    private final CliIO io;
    private final SessionContext session;

    public StudentMenu(CliIO io, SessionContext session) {
        this.io = io;
        this.session = session;
    }

    public void run() {
        boolean loop = true;
        Student student = (Student) session.currentUser();
        while (loop) {
            int choice = io.chooseMenu("Student", List.of(
                    "Course catalog",
                    "Request course registration",
                    "My registrations",
                    "My courses",
                    "My marks",
                    "Transcript / GPA",
                    "Rate teacher",
                    "My report",
                    "Become researcher",
                    "Leave researcher role",
                    "Research menu",
                    "Graduate: supervisor",
                    "Graduate: diploma project"));
            try {
                switch (choice) {
                    case 1 -> session.services().courseAdminService.listCourses(session.currentUser())
                            .forEach(c -> io.println(c + " eligible="
                                    + c.isEligible(student)));
                    case 2 -> requestRegistration(student);
                    case 3 -> listMyRegistrations(student);
                    case 4 -> student.viewCourses().forEach(c -> io.println(c.toString()));
                    case 5 -> student.viewMarks().forEach(m -> io.println("Total=" + m.getTotal()));
                    case 6 -> {
                        io.println(student.getTranscript().toString());
                        io.println("GPA: " + String.format("%.2f", student.getGpa()));
                    }
                    case 7 -> rateTeacher();
                    case 8 -> io.println(session.services().reportingService.studentReport(session.currentUser(), student));
                    case 9 -> {
                        session.services().studentProfileService.becomeResearcher(student);
                        io.println("Researcher mode enabled.");
                    }
                    case 10 -> {
                        session.services().studentProfileService.leaveResearcher(student);
                        io.println("Researcher mode disabled.");
                    }
                    case 11 -> {
                        if (student.isResearcherActive()) {
                            ResearchMenuHelper.researchSubmenu(io, session, student);
                        } else {
                            io.println("Become a researcher first.");
                        }
                    }
                    case 12 -> setSupervisor();
                    case 13 -> setDiploma();
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

    private void requestRegistration(Student student) {
        String courseId = io.readLine("Course ID: ");
        CourseRegistration reg = session.services().registrationService.createRequest(session.currentUser(), student,
                courseId);
        io.println("Request created: " + reg.getStatus());
    }

    private void listMyRegistrations(Student student) {
        session.services().registrationService.listAll().stream()
                .filter(r -> r.getStudent().getId().equals(student.getId()))
                .forEach(r -> io.println(r.getCourse().getId() + " " + r.getStatus()));
    }

    private void rateTeacher() {
        String login = io.readLine("Teacher login: ");
        int rating = io.readInt("Rating 1-5: ");
        session.services().studentProfileService.rateTeacher(session.currentUser(), login, rating);
        io.println("Rating saved.");
    }

    private void setSupervisor() throws LowHIndexException {
        if (!(session.currentUser() instanceof GraduateStudent)) {
            io.println("Only graduate students.");
            return;
        }
        String login = io.readLine("Supervisor login (empty to clear): ");
        session.services().studentProfileService.setSupervisor(session.currentUser(), login);
        io.println("Supervisor updated.");
    }

    private void setDiploma() {
        if (!(session.currentUser() instanceof GraduateStudent)) {
            io.println("Only graduate students.");
            return;
        }
        String title = io.readLine("Project title: ");
        String desc = io.readLine("Description: ");
        session.services().studentProfileService.setDiplomaProject(session.currentUser(), title, desc);
        if (session.currentUser() instanceof MasterStudent m) {
            io.println("Diploma set for master student.");
        } else if (session.currentUser() instanceof PhDStudent p) {
            io.println("Diploma set for PhD student.");
        }
    }
}
