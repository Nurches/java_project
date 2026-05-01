package communication;

import java.io.Serializable;
import java.time.LocalDateTime;

import users.User;

public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    private User author;
    private String text;
    private LocalDateTime createdAt;

    public Comment(User author, String text) {
        this.author = author;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
