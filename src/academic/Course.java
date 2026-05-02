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
    private String intendedMajor;
    private int intendedYearOfStudy;
    private List<Teacher> instructors;
    private List<Student> students;
    private List<Lesson> lessons;

    public Course(String id, String name, int credits, CourseType courseType, Language language) {
        this(id, name, credits, courseType, language, "ANY", 0);
    }

    public Course(String id, String name, int credits, CourseType courseType, Language language,
            String intendedMajor, int intendedYearOfStudy) {
        if (credits <= 0) {
            throw new IllegalArgumentException("Credits must be positive");
        }

        this.id = id;
        this.name = name;
        this.credits = credits;
        this.courseType = courseType;
        this.language = language;
        this.intendedMajor = intendedMajor == null || intendedMajor.isBlank() ? "ANY" : intendedMajor;
        this.intendedYearOfStudy = Math.max(0, intendedYearOfStudy);
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

    public String getIntendedMajor() {
        return intendedMajor;
    }

    public int getIntendedYearOfStudy() {
        return intendedYearOfStudy;
    }

    public List<Teacher> getInstructors() {
        return new ArrayList<>(instructors);
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons);
    }

    public void addInstructor(Teacher teacher) {
        if (teacher != null && !instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
        }
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public void addLesson(Lesson lesson) {
        if (lesson != null) {
            lessons.add(lesson);
        }
    }

    public boolean isEligible(Student student) {
        boolean majorMatched = "ANY".equalsIgnoreCase(intendedMajor)
                || intendedMajor.equalsIgnoreCase(student.getMajor());
        boolean yearMatched = intendedYearOfStudy <= 0 || intendedYearOfStudy == student.getYearOfStudy();
        return majorMatched && yearMatched;
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

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", major='" + intendedMajor + '\'' +
                ", year=" + intendedYearOfStudy +
                '}';
    }
}
