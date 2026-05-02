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

    public void putMark(Student student, Course course, Mark mark) {
        if (!courses.contains(course)) {
            throw new IllegalArgumentException("Teacher is not assigned to this course");
        }
        if (!course.getStudents().contains(student) || !student.hasCourse(course)) {
            throw new IllegalArgumentException("Student is not registered in this course");
        }
        student.addMark(course, mark);
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
        return researcherActive;
    }

    public void becomeResearcher() {
        this.researcherActive = true;
    }

    public void leaveResearcherRole() {
        if (title == TeacherTitle.PROFESSOR) {
            return;
        }
        this.researcherActive = false;
    }

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
