package users;

import java.util.ArrayList;
import java.util.List;

import academic.Course;
import academic.Mark;
import academic.Transcript;
import enums.UserRole;
import exceptions.CourseAlreadyRegisteredException;
import exceptions.CreditLimitException;

public class Student extends User {
    private static final long serialVersionUID = 1L;

    protected String major;
    protected int yearOfStudy;
    protected double gpa;
    protected int credits;
    protected List<Course> courses;
    protected List<Mark> marks;

    public Student(String id, String login, String password, String name, String email, String major, int yearOfStudy) {
        super(id, login, password, name, email, UserRole.STUDENT);
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        this.gpa = 0.0;
        this.credits = 0;
        this.courses = new ArrayList<>();
        this.marks = new ArrayList<>();
    }

    public void registerForCourse(Course course) throws CreditLimitException, CourseAlreadyRegisteredException {
        if (courses.contains(course)) {
            throw new CourseAlreadyRegisteredException("Student is already registered for this course");
        }
        if (credits + course.getCredits() > 21) {
            throw new CreditLimitException("Credit limit exceeded. Max allowed credits is 21");
        }
        courses.add(course);
        credits += course.getCredits();
        course.addStudent(this);
    }

    public List<Course> viewCourses() {
        return new ArrayList<>(courses);
    }

    public List<Mark> viewMarks() {
        return new ArrayList<>(marks);
    }

    public Transcript getTranscript() {
        return new Transcript(id, marks, calculateGpa());
    }

    public double calculateGpa() {
        if (marks.isEmpty()) {
            gpa = 0.0;
            return gpa;
        }

        double avg = marks.stream().mapToDouble(Mark::getTotal).average().orElse(0.0);
        gpa = Math.min(4.0, (avg / 100.0) * 4.0);
        return gpa;
    }

    public void addMark(Mark mark) {
        marks.add(mark);
        calculateGpa();
    }

    public String getMajor() {
        return major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public double getGpa() {
        return gpa;
    }

    public int getCredits() {
        return credits;
    }
}
