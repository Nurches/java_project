package users;

import java.util.ArrayList;
import java.util.List;

import academic.Course;
import academic.Mark;
import communication.Complaint;
import enums.TeacherTitle;
import enums.UrgencyLevel;
import enums.UserRole;

public class Teacher extends Employee {
    private static final long serialVersionUID = 1L;

    private TeacherTitle title;
    private List<Course> courses;

    public Teacher(String id, String login, String password, String name, String email, double salary, String department,
            TeacherTitle title) {
        super(id, login, password, name, email, UserRole.TEACHER, salary, department);
        this.title = title;
        this.courses = new ArrayList<>();
    }

    public TeacherTitle getTitle() {
        return title;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void putMark(Student student, Course course, Mark mark) {
        // TODO Validate that student belongs to this teacher's course before grading.
        student.addMark(mark);
    }

    public Complaint sendComplaint(Student student, String text, UrgencyLevel urgency) {
        return new Complaint(this, student, text, urgency);
    }

    public void assignCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.addInstructor(this);
        }
    }

    public boolean canActAsResearcher() {
        // Placeholder for future extension (e.g., professor + h-index checks).
        return title == TeacherTitle.PROFESSOR;
    }
}
