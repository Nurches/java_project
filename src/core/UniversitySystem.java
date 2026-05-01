package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import communication.Message;
import communication.News;
import research.ResearchPaper;
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
        users.add(user);
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
        courses.add(course);
    }

    public void addNews(News newItem) {
        news.add(newItem);
    }

    public void addResearchPaper(ResearchPaper paper) {
        researchPapers.add(paper);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void printAllResearchPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(comparator);
        sorted.forEach(System.out::println);
    }

    public List<User> getUsers() {
        return users;
    }

    public List<academic.Course> getCourses() {
        return courses;
    }

    public List<ResearchPaper> getResearchPapers() {
        return researchPapers;
    }

    public List<News> getNews() {
        return news;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
