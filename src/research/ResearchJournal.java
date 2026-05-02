package research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import communication.Message;
import core.UniversitySystem;
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
        return new ArrayList<>(subscribers);
    }

    public List<ResearchPaper> getPapers() {
        return new ArrayList<>(papers);
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
        for (User subscriber : subscribers) {
            Message notification = new Message(subscriber, subscriber,
                    "Journal '" + name + "' published: " + paper.getTitle());
            notification.markAsRead();
            UniversitySystem.getInstance().addMessage(notification);
            System.out.println("Notify " + subscriber.getName() + ": New paper published -> " + paper.getTitle());
        }
    }
}
