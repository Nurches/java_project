package users;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import academic.Course;
import academic.Mark;
import academic.Transcript;
import enums.UserRole;
import exceptions.CourseAlreadyRegisteredException;
import exceptions.CreditLimitException;
import research.ResearchPaper;
import research.Researcher;

/**
 * Represents a student who can enroll in courses, receive marks, calculate GPA,
 * and optionally participate in research activity.
 */
public class Student extends User implements Researcher {
    private static final long serialVersionUID = 1L;

    protected String major;
    protected int yearOfStudy;
    protected double gpa;
    protected int credits;
    protected List<Course> courses;
    protected List<Mark> marks;
    protected Map<String, Mark> marksByCourse;
    protected int failedCoursesCount;
    protected boolean researcherActive;
    protected List<ResearchPaper> researchPapers;

    public Student(String id, String login, String password, String name, String email, String major, int yearOfStudy) {
        super(id, login, password, name, email, UserRole.STUDENT);
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        this.gpa = 0.0;
        this.credits = 0;
        this.courses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.marksByCourse = new HashMap<>();
        this.failedCoursesCount = 0;
        this.researcherActive = false;
        this.researchPapers = new ArrayList<>();
    }

    /**
     * Registers the student for a course if duplicate and credit-limit checks pass.
     *
     * @param course the course to register for
     * @throws CreditLimitException if total credits would exceed 21
     * @throws CourseAlreadyRegisteredException if the student is already enrolled
     */
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

    /**
     * Recalculates GPA based on the student's current marks.
     *
     * @return GPA on a 4.0 scale
     */
    public double calculateGpa() {
        if (marks.isEmpty()) {
            gpa = 0.0;
            return gpa;
        }

        double avg = marks.stream().mapToDouble(Mark::getTotal).average().orElse(0.0);
        gpa = Math.min(4.0, (avg / 100.0) * 4.0);
        return gpa;
    }

    /**
     * Adds or replaces a mark for the given course and refreshes GPA and failed
     * course statistics.
     *
     * @param course the course for which the mark is assigned
     * @param mark the mark to store
     */
    public void addMark(Course course, Mark mark) {
        Mark previous = marksByCourse.put(course.getId(), mark);
        if (previous != null) {
            marks.remove(previous);
            if (!previous.isPassed()) {
                failedCoursesCount = Math.max(0, failedCoursesCount - 1);
            }
        }

        marks.add(mark);
        if (!mark.isPassed()) {
            failedCoursesCount++;
        }
        calculateGpa();
    }

    public boolean hasCourse(Course course) {
        return courses.contains(course);
    }

    public boolean hasPassedCourse(String courseId) {
        if (courseId == null || courseId.isBlank()) {
            return false;
        }
        Mark mark = marksByCourse.get(courseId.trim());
        return mark != null && mark.isPassed();
    }

    public void rateTeacher(Teacher teacher, int rating) {
        teacher.acceptRating(this, rating);
    }

    public String getMajor() {
        return major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setMajor(String major) {
        if (major != null && !major.isBlank()) {
            this.major = major.trim();
        }
    }

    public void setYearOfStudy(int yearOfStudy) {
        if (yearOfStudy > 0) {
            this.yearOfStudy = yearOfStudy;
        }
    }

    public double getGpa() {
        return gpa;
    }

    public int getCredits() {
        return credits;
    }

    public int getFailedCoursesCount() {
        return failedCoursesCount;
    }

    /**
     * Checks whether the student is still eligible to register for new courses.
     *
     * @return {@code true} if failed courses count is below the limit
     */
    public boolean canRegisterForMoreCourses() {
        return failedCoursesCount < 3;
    }

    /**
     * Activates researcher mode for the student.
     */
    public void becomeResearcher() {
        this.researcherActive = true;
    }

    /**
     * Deactivates researcher mode for the student.
     */
    public void leaveResearcherRole() {
        this.researcherActive = false;
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(researchPapers);
    }

    @Override
    /**
     * Adds a research paper to the student's profile.
     *
     * @param paper the paper to add
     * @throws IllegalStateException if researcher mode is not active
     */
    public void addResearchPaper(ResearchPaper paper) {
        if (!researcherActive) {
            throw new IllegalStateException("Student is not an active researcher");
        }
        if (!researchPapers.contains(paper)) {
            researchPapers.add(paper);
        }
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        if (!researcherActive) {
            throw new IllegalStateException("Student is not an active researcher");
        }
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(comparator);
        sorted.forEach(System.out::println);
    }

    @Override
    /**
     * Calculates the h-index from the student's research papers.
     *
     * @return the h-index, or 0 if researcher mode is inactive
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
