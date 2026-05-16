package app.service;

import app.audit.AuditLogger;
import communication.Comment;
import communication.News;
import core.UniversitySystem;
import users.User;

public class CommentService {
    private final AuditLogger auditLogger;

    public CommentService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    public void addComment(User actor, int newsIndex, String text) {
        java.util.List<News> newsList = UniversitySystem.getInstance().getNews();
        if (newsIndex < 0 || newsIndex >= newsList.size()) {
            throw new IllegalArgumentException("Invalid news index");
        }
        News news = newsList.get(newsIndex);
        news.addComment(new Comment(actor, text));
        auditLogger.log(actor.getLogin(), "ADD_COMMENT", "news", news.getTitle());
    }
}
