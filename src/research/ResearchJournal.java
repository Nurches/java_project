package research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import users.User;

public class ResearchJournal implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<User> subscribers;
    private List<ResearchPaper> papers;

    public ResearchJournal(String name) {
        this.name = name;
        this.subscribers = new ArrayList<>();
        this.papers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
        }
    }

    public void unsubscribe(User user) {
        subscribers.remove(user);
    }

    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        notifySubscribers(paper);
    }

    public void notifySubscribers(ResearchPaper paper) {
        // TODO Observer pattern notification dispatch to user inbox/notification service
        for (User subscriber : subscribers) {
            System.out.println("Notify " + subscriber.getName() + ": New paper published -> " + paper.getTitle());
        }
    }
}
