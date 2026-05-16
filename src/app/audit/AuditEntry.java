package app.audit;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AuditEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LocalDateTime createdAt;
    private final String actorLogin;
    private final String action;
    private final String entity;
    private final String details;

    public AuditEntry(String actorLogin, String action, String entity, String details) {
        this.createdAt = LocalDateTime.now();
        this.actorLogin = actorLogin;
        this.action = action;
        this.entity = entity;
        this.details = details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getActorLogin() {
        return actorLogin;
    }

    public String getAction() {
        return action;
    }

    public String getEntity() {
        return entity;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "[" + createdAt + "] actor=" + actorLogin + " action=" + action + " entity=" + entity + " details="
                + details;
    }
}
