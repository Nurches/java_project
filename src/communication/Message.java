package communication;

import java.io.Serializable;
import java.time.LocalDateTime;

import enums.MessageStatus;
import users.User;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private User sender;
    private User receiver;
    private String text;
    private MessageStatus status;
    private LocalDateTime sentAt;

    public Message(User sender, User receiver, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.status = MessageStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void markAsRead() {
        this.status = MessageStatus.READ;
    }
}
