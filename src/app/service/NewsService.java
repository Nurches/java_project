package app.service;

import java.util.List;

import app.audit.AuditLogger;
import core.UniversitySystem;
import enums.UserRole;
import communication.News;
import users.User;

public class NewsService {
    private final RbacService rbacService;
    private final AuditLogger auditLogger;

    public NewsService(RbacService rbacService, AuditLogger auditLogger) {
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    public void publish(User actor, News news) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        UniversitySystem.getInstance().addNews(news);
        auditLogger.log(actor.getLogin(), "PUBLISH_NEWS", "news", news.getTitle());
    }

    public List<News> getAll() {
        return UniversitySystem.getInstance().getNews();
    }
}
