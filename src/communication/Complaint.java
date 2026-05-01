package communication;

import java.io.Serializable;
import java.time.LocalDateTime;

import enums.UrgencyLevel;
import users.Student;
import users.Teacher;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    private Teacher sender;
    private Student targetStudent;
    private String text;
    private UrgencyLevel urgencyLevel;
    private LocalDateTime createdAt;

    public Complaint(Teacher sender, Student targetStudent, String text, UrgencyLevel urgencyLevel) {
        this.sender = sender;
        this.targetStudent = targetStudent;
        this.text = text;
        this.urgencyLevel = urgencyLevel;
        this.createdAt = LocalDateTime.now();
    }

    public Teacher getSender() {
        return sender;
    }

    public Student getTargetStudent() {
        return targetStudent;
    }

    public String getText() {
        return text;
    }

    public UrgencyLevel getUrgencyLevel() {
        return urgencyLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
