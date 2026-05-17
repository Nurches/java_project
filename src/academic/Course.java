package academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import enums.CourseType;
import enums.Language;
import users.Student;
import users.Teacher;

/**
 * Represents a university course with eligibility rules, instructors, students,
 * and scheduled lessons.
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private int credits;
    private CourseType courseType;
    private Language language;
    private String intendedMajor;
    private int intendedYearOfStudy;
    private int maxStudents;
    private List<String> prerequisiteCourseIds;
    private List<Teacher> instructors;
    private List<Student> students;
    private List<Lesson> lessons;
    public static final int DEFAULT_MAX_STUDENTS = 30;

    public Course(String id, String name, int credits, CourseType courseType, Language language) {
        this(id, name, credits, courseType, language, "ANY", 0, DEFAULT_MAX_STUDENTS, List.of());
    }

    public Course(String id, String name, int credits, CourseType courseType, Language language,
            String intendedMajor, int intendedYearOfStudy) {
        this(id, name, credits, courseType, language, intendedMajor, intendedYearOfStudy, DEFAULT_MAX_STUDENTS,
                List.of());
    }

    public Course(String id, String name, int credits, CourseType courseType, Language language,
            String intendedMajor, int intendedYearOfStudy, int maxStudents, List<String> prerequisiteCourseIds) {
        if (credits <= 0) {
            throw new IllegalArgumentException("Credits must be positive");
        }
        if (maxStudents <= 0) {
            throw new IllegalArgumentException("Course capacity must be positive");
        }

        this.id = id;
        this.name = name;
        this.credits = credits;
        this.courseType = courseType;
        this.language = language;
        this.intendedMajor = intendedMajor == null || intendedMajor.isBlank() ? "ANY" : intendedMajor;
        this.intendedYearOfStudy = Math.max(0, intendedYearOfStudy);
        this.maxStudents = maxStudents;
        this.prerequisiteCourseIds = new ArrayList<>();
        if (prerequisiteCourseIds != null) {
            for (String prerequisiteCourseId : prerequisiteCourseIds) {
                if (prerequisiteCourseId != null && !prerequisiteCourseId.isBlank()) {
                    this.prerequisiteCourseIds.add(prerequisiteCourseId.trim());
                }
            }
        }
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

    public int getMaxStudents() {
        return maxStudents;
    }

    public List<String> getPrerequisiteCourseIds() {
        return Collections.unmodifiableList(prerequisiteCourseIds);
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

    /**
     * Assigns an instructor to the course.
     *
     * @param teacher the teacher to add
     */
    public void addInstructor(Teacher teacher) {
        if (teacher != null && !instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    /**
     * Adds a student to the enrolled students list.
     *
     * @param student the student to enroll
     */
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

    /**
     * Checks whether a student matches the course major and year requirements.
     *
     * @param student the student being evaluated
     * @return {@code true} if the student may register for this course
     */
    public boolean isEligible(Student student) {
        boolean majorMatched = "ANY".equalsIgnoreCase(intendedMajor)
                || intendedMajor.equalsIgnoreCase(student.getMajor());
        boolean yearMatched = intendedYearOfStudy <= 0 || intendedYearOfStudy == student.getYearOfStudy();
        return majorMatched && yearMatched;
    }

    public boolean hasCapacity() {
        return students.size() < maxStudents;
    }

    public List<String> getMissingPrerequisites(Student student) {
        List<String> missing = new ArrayList<>();
        for (String prerequisiteCourseId : prerequisiteCourseIds) {
            if (!student.hasPassedCourse(prerequisiteCourseId)) {
                missing.add(prerequisiteCourseId);
            }
        }
        return missing;
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
                ", capacity=" + students.size() + "/" + maxStudents +
                '}';
    }
}
