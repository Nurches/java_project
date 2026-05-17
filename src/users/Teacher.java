package users;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import academic.Course;
import academic.Mark;
import communication.Complaint;
import enums.TeacherTitle;
import enums.UrgencyLevel;
import enums.UserRole;
import research.ResearchPaper;
import research.Researcher;

/**
 * Represents a teacher who can teach courses, grade students, receive ratings,
 * and optionally participate in research.
 */
public class Teacher extends Employee implements Researcher {
    private static final long serialVersionUID = 1L;

    private TeacherTitle title;
    private List<Course> courses;
    private List<ResearchPaper> researchPapers;
    private Map<String, Integer> ratingsByStudentId;
    private boolean researcherActive;

    public Teacher(String id, String login, String password, String name, String email, double salary, String department,
            TeacherTitle title) {
        super(id, login, password, name, email, UserRole.TEACHER, salary, department);
        this.title = title;
        this.courses = new ArrayList<>();
        this.researchPapers = new ArrayList<>();
        this.ratingsByStudentId = new HashMap<>();
        this.researcherActive = title == TeacherTitle.PROFESSOR;
    }

    public TeacherTitle getTitle() {
        return title;
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    /**
     * Assigns a mark to a student for one of the teacher's own courses.
     *
     * @param student the student being graded
     * @param course the course in which grading happens
     * @param mark the mark to assign
     */
    public void putMark(Student student, Course course, Mark mark) {
        if (!courses.contains(course)) {
            throw new IllegalArgumentException("Teacher is not assigned to this course");
        }
        if (!course.getStudents().contains(student) || !student.hasCourse(course)) {
            throw new IllegalArgumentException("Student is not registered in this course");
        }
        student.addMark(course, mark);
    }

    /**
     * Creates a complaint against a student.
     *
     * @param student the target student
     * @param text complaint text
     * @param urgency urgency level
     * @return created complaint object
     */
    public Complaint sendComplaint(Student student, String text, UrgencyLevel urgency) {
        return new Complaint(this, student, text, urgency);
    }

    /**
     * Assigns a course to the teacher and registers the teacher as an instructor.
     *
     * @param course the course to assign
     */
    public void assignCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.addInstructor(this);
        }
    }

    public boolean canActAsResearcher() {
        return researcherActive;
    }

    /**
     * Activates researcher mode for the teacher.
     */
    public void becomeResearcher() {
        this.researcherActive = true;
    }

    /**
     * Deactivates researcher mode for non-professor teachers.
     */
    public void leaveResearcherRole() {
        if (title == TeacherTitle.PROFESSOR) {
            return;
        }
        this.researcherActive = false;
    }

    /**
     * Stores a rating submitted by a student.
     *
     * @param student the student who submitted the rating
     * @param rating teacher rating from 1 to 5
     */
    public void acceptRating(Student student, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        ratingsByStudentId.put(student.getId(), rating);
    }

    public double getAverageRating() {
        return ratingsByStudentId.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(researchPapers);
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        if (!researcherActive) {
            throw new IllegalStateException("Teacher is not an active researcher");
        }
        if (!researchPapers.contains(paper)) {
            researchPapers.add(paper);
        }
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        if (!researcherActive) {
            throw new IllegalStateException("Teacher is not an active researcher");
        }
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(comparator);
        sorted.forEach(System.out::println);
    }

    @Override
    /**
     * Calculates the teacher's h-index from the current research papers list.
     *
     * @return h-index, or 0 if researcher mode is inactive
     */
    public int calculateHIndex() {
        if (!researcherActive) {
            return 0;
        }
        List<Integer> citations = researchPapers.stream()
                .map(ResearchPaper::getCitations)
                .sorted(Comparator.reverseOrder())
                .toList();

        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }

    @Override
    public int getTotalCitations() {
        if (!researcherActive) {
            return 0;
        }
        return researchPapers.stream().mapToInt(ResearchPaper::getCitations).sum();
    }

    @Override
    public boolean isResearcherActive() {
        return researcherActive;
    }
}
