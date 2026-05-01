package academic;

import java.io.Serializable;
import java.time.LocalDateTime;

import enums.LessonType;

public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String topic;
    private LessonType lessonType;
    private LocalDateTime dateTime;

    public Lesson(String id, String topic, LessonType lessonType, LocalDateTime dateTime) {
        this.id = id;
        this.topic = topic;
        this.lessonType = lessonType;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
