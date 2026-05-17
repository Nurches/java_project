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

    public boolean canRegisterForMoreCourses() {
        return failedCoursesCount < 3;
    }

    public void becomeResearcher() {
        this.researcherActive = true;
    }

    public void leaveResearcherRole() {
        this.researcherActive = false;
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(researchPapers);
    }

    @Override
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
