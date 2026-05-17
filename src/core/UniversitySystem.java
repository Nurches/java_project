package core;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import academic.CourseRegistration;
import app.audit.AuditEntry;
import communication.Complaint;
import communication.EmployeeRequest;
import communication.Message;
import communication.News;
import research.ResearchJournal;
import research.ResearchPaper;
import research.ResearchProject;
import research.Researcher;
import users.Student;
import users.Teacher;
import users.User;

public class UniversitySystem implements Serializable {
    private static final long serialVersionUID = 3L;
    private static UniversitySystem instance;

    private List<User> users;
    private List<academic.Course> courses;
    private List<ResearchPaper> researchPapers;
    private List<News> news;
    private List<Message> messages;
    private List<CourseRegistration> registrations;
    private List<Complaint> complaints;
    private List<ResearchProject> researchProjects;
    private ResearchJournal researchJournal;
    private List<AuditEntry> auditEntries;
    private List<EmployeeRequest> employeeRequests;

    private UniversitySystem() {
        this.users = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.researchPapers = new ArrayList<>();
        this.news = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.registrations = new ArrayList<>();
        this.complaints = new ArrayList<>();
        this.researchProjects = new ArrayList<>();
        this.researchJournal = new ResearchJournal("University Research Journal");
        this.auditEntries = new ArrayList<>();
        this.employeeRequests = new ArrayList<>();
    }

    public static UniversitySystem getInstance() {
        if (instance == null) {
            instance = new UniversitySystem();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = new UniversitySystem();
    }

    public static void replaceInstance(UniversitySystem loaded) {
        instance = loaded;
        if (loaded.registrations == null) {
            loaded.registrations = new ArrayList<>();
        }
        if (loaded.complaints == null) {
            loaded.complaints = new ArrayList<>();
        }
        if (loaded.researchProjects == null) {
            loaded.researchProjects = new ArrayList<>();
        }
        if (loaded.auditEntries == null) {
            loaded.auditEntries = new ArrayList<>();
        }
        if (loaded.researchJournal == null) {
            loaded.researchJournal = new ResearchJournal("University Research Journal");
        }
        if (loaded.employeeRequests == null) {
            loaded.employeeRequests = new ArrayList<>();
        }
    }

    public void addUser(User user) {
        if (user == null) {
            return;
        }
        if (findUserByLogin(user.getLogin()) != null) {
            throw new IllegalArgumentException("Login already exists: " + user.getLogin());
        }
        if (findUserById(user.getId()) != null) {
            throw new IllegalArgumentException("User id already exists: " + user.getId());
        }
        users.add(user);
    }

    public User findUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
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

    public void addRegistration(CourseRegistration registration) {
        if (registration != null && !registrations.contains(registration)) {
            registrations.add(registration);
        }
    }

    public List<CourseRegistration> getRegistrations() {
        return new ArrayList<>(registrations);
    }

    public void addComplaint(Complaint complaint) {
        if (complaint != null) {
            complaints.add(complaint);
        }
    }

    public List<Complaint> getComplaints() {
        return new ArrayList<>(complaints);
    }

    public void addEmployeeRequest(EmployeeRequest request) {
        if (request != null && !employeeRequests.contains(request)) {
            employeeRequests.add(request);
        }
    }

    public List<EmployeeRequest> getEmployeeRequests() {
        return new ArrayList<>(employeeRequests);
    }

    public EmployeeRequest findEmployeeRequestById(String id) {
        for (EmployeeRequest request : employeeRequests) {
            if (request.getId().equals(id)) {
                return request;
            }
        }
        return null;
    }

    public void addResearchProject(ResearchProject project) {
        if (project != null && !researchProjects.contains(project)) {
            researchProjects.add(project);
        }
    }

    public List<ResearchProject> getResearchProjects() {
        return new ArrayList<>(researchProjects);
    }

    public ResearchJournal getResearchJournal() {
        return researchJournal;
    }

    public void setResearchJournal(ResearchJournal journal) {
        this.researchJournal = journal;
    }

    public void addAuditEntry(AuditEntry entry) {
        if (entry != null) {
            auditEntries.add(entry);
        }
    }

    public List<AuditEntry> getAuditEntries() {
        return new ArrayList<>(auditEntries);
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
