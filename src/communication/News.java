package communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import enums.NewsTopic;

public class News implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private NewsTopic topic;
    private boolean pinned;
    private List<Comment> comments;

    public News(String title, String content, NewsTopic topic) {
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.pinned = topic == NewsTopic.RESEARCH;
        this.comments = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public NewsTopic getTopic() {
        return topic;
    }

    public boolean isPinned() {
        return pinned;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
