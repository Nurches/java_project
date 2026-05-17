package app.cli.menu;

import java.util.List;

import app.cli.CliIO;
import app.cli.SessionContext;
import academic.CourseRegistration;
import exceptions.LowHIndexException;
import users.BachelorStudent;
import users.GraduateStudent;
import users.MasterStudent;
import users.PhDStudent;
import users.Student;
import users.Teacher;

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
                    "View teachers for course",
                    "My marks",
                    "Transcript / GPA",
                    "Rate teacher",
                    "My report",
                    "Become researcher",
                    "Leave researcher role",
                    "Research menu",
                    "4th year: set supervisor",
                    "Graduate: diploma project"));
            try {
                switch (choice) {
                    case 1 -> session.services().courseAdminService.listCourses(session.currentUser())
                            .forEach(c -> io.println(c + " eligible="
                                    + c.isEligible(student)));
                    case 2 -> requestRegistration(student);
                    case 3 -> listMyRegistrations(student);
                    case 4 -> student.viewCourses().forEach(c -> io.println(c.toString()));
                    case 5 -> viewTeachersForCourse();
                    case 6 -> student.viewMarks().forEach(m -> io.println("Total=" + m.getTotal()));
                    case 7 -> {
                        io.println(student.getTranscript().toString());
                        io.println("GPA: " + String.format("%.2f", student.getGpa()));
                    }
                    case 8 -> rateTeacher();
                    case 9 -> io.println(session.services().reportingService.studentReport(session.currentUser(), student));
                    case 10 -> {
                        session.services().studentProfileService.becomeResearcher(student);
                        io.println("Researcher mode enabled.");
                    }
                    case 11 -> {
                        session.services().studentProfileService.leaveResearcher(student);
                        io.println("Researcher mode disabled.");
                    }
                    case 12 -> {
                        if (student.isResearcherActive()) {
                            ResearchMenuHelper.researchSubmenu(io, session, student);
                        } else {
                            io.println("Become a researcher first.");
                        }
                    }
                    case 13 -> setSupervisor();
                    case 14 -> setDiploma();
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

    private void viewTeachersForCourse() {
        String courseId = io.readLine("Course ID: ");
        List<Teacher> teachers = session.services().studentProfileService.teachersForCourse(session.currentUser(),
                courseId);
        if (teachers.isEmpty()) {
            io.println("No instructors assigned to this course.");
            return;
        }
        teachers.forEach(t -> io.println(t.getName() + " | login=" + t.getLogin()
                + " | title=" + t.getTitle()
                + " | rating=" + String.format("%.2f", t.getAverageRating())
                + " | dept=" + t.getDepartment()));
    }

    private void rateTeacher() {
        String login = io.readLine("Teacher login: ");
        int rating = io.readInt("Rating 1-5: ");
        session.services().studentProfileService.rateTeacher(session.currentUser(), login, rating);
        io.println("Rating saved.");
    }

    private void setSupervisor() throws LowHIndexException {
        if (!(session.currentUser() instanceof BachelorStudent bachelor)) {
            io.println("Only bachelor students can set a supervisor.");
            return;
        }
        if (bachelor.getYearOfStudy() != 4) {
            io.println("Supervisor is only for 4th year bachelor students (your year: " + bachelor.getYearOfStudy() + ").");
            return;
        }
        String login = io.readLine("Supervisor login (empty to clear): ");
        session.services().studentProfileService.setSupervisor(session.currentUser(), login);
        io.println("Supervisor updated.");
    }

    private void setDiploma() {
        if (!(session.currentUser() instanceof GraduateStudent)) {
            io.println("Only graduate students (master/PhD).");
            return;
        }
        String title = io.readLine("Project title: ");
        String desc = io.readLine("Description: ");
        session.services().studentProfileService.setDiplomaProject(session.currentUser(), title, desc);
        if (session.currentUser() instanceof MasterStudent) {
            io.println("Diploma set for master student.");
        } else if (session.currentUser() instanceof PhDStudent) {
            io.println("Diploma set for PhD student.");
        }
    }
}
