package app.audit;

import java.util.ArrayList;
import java.util.List;

import core.UniversitySystem;

public class AuditLogger {
    public void log(String actorLogin, String action, String entity, String details) {
        AuditEntry entry = new AuditEntry(actorLogin, action, entity, details);
        UniversitySystem.getInstance().addAuditEntry(entry);
        System.out.println("[AUDIT] " + entry);
    }

    public List<AuditEntry> recent(int limit) {
        List<AuditEntry> all = UniversitySystem.getInstance().getAuditEntries();
        int from = Math.max(0, all.size() - limit);
        return new ArrayList<>(all.subList(from, all.size()));
    }
}
