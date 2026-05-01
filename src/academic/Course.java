package academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import enums.CourseType;
import enums.Language;
import users.Student;
import users.Teacher;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private int credits;
    private CourseType courseType;
    private Language language;
    private List<Teacher> instructors;
    private List<Student> students;
    private List<Lesson> lessons;

    public Course(String id, String name, int credits, CourseType courseType, Language language) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.courseType = courseType;
        this.language = language;
        this.instructors = new ArrayList<>();
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public Language getLanguage() {
        return language;
    }

    public List<Teacher> getInstructors() {
        return instructors;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void addInstructor(Teacher teacher) {
        if (!instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
        }
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Course)) {
            return false;
        }
        Course other = (Course) obj;
        return Objects.equals(id, other.id);
    }
}
