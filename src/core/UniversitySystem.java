package core;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import communication.Message;
import communication.News;
import research.ResearchPaper;
import research.Researcher;
import users.Student;
import users.Teacher;
import users.User;

public class UniversitySystem implements Serializable {
    private static final long serialVersionUID = 1L;
    private static UniversitySystem instance;

    private List<User> users;
    private List<academic.Course> courses;
    private List<ResearchPaper> researchPapers;
    private List<News> news;
    private List<Message> messages;

    private UniversitySystem() {
        this.users = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.researchPapers = new ArrayList<>();
        this.news = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public static UniversitySystem getInstance() {
        if (instance == null) {
            instance = new UniversitySystem();
        }
        return instance;
    }

    public void addUser(User user) {
        if (user != null && !users.contains(user)) {
            users.add(user);
        }
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public User findUserByLogin(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    public void addCourse(academic.Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
        }
    }

    public void addNews(News newItem) {
        if (newItem != null) {
            news.add(newItem);
        }
    }

    public void addResearchPaper(ResearchPaper paper) {
        if (paper != null && !researchPapers.contains(paper)) {
            researchPapers.add(paper);
        }
    }

    public void addMessage(Message message) {
        if (message != null) {
            messages.add(message);
        }
    }

    public void printAllResearchPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(comparator);
        sorted.forEach(System.out::println);
    }

    public void printAllResearchersPapers(Comparator<ResearchPaper> comparator) {
        for (Researcher researcher : getResearchers()) {
            researcher.printPapers(comparator);
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public List<academic.Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(researchPapers);
    }

    public List<News> getNews() {
        return new ArrayList<>(news);
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Student student) {
                students.add(student);
            }
        }
        return students;
    }

    public List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Teacher teacher) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public List<Researcher> getResearchers() {
        List<Researcher> researchers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Researcher researcher && researcher.isResearcherActive()) {
                researchers.add(researcher);
            }
        }
        return researchers;
    }

    public Optional<Researcher> getTopCitedResearcherBySchool(String school) {
        return getResearchers().stream()
                .filter(r -> r.getResearchPapers().stream().anyMatch(p -> p.getSchool().equalsIgnoreCase(school)))
                .max(Comparator.comparingInt(Researcher::getTotalCitations));
    }

    public Optional<Researcher> getTopCitedResearcherOfYear(int year) {
        return getResearchers().stream()
                .max(Comparator.comparingInt(r -> citationsForYear(r, year)));
    }

    public Optional<Researcher> getTopCitedResearcherOfCurrentYear() {
        return getTopCitedResearcherOfYear(Year.now().getValue());
    }

    private int citationsForYear(Researcher researcher, int year) {
        return researcher.getResearchPapers().stream()
                .filter(p -> p.getPublishedDate().getYear() == year)
                .mapToInt(ResearchPaper::getCitations)
                .sum();
    }
}
