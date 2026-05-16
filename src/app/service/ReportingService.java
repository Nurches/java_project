package app.service;

import app.audit.AuditLogger;
import enums.UserRole;
import reports.ReportGenerator;
import users.Student;
import users.User;

public class ReportingService {
    private final RbacService rbacService;
    private final AuditLogger auditLogger;
    private final ReportGenerator reportGenerator;

    public ReportingService(RbacService rbacService, AuditLogger auditLogger) {
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
        this.reportGenerator = new ReportGenerator();
    }

    public String systemReport(User actor) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        auditLogger.log(actor.getLogin(), "SYSTEM_REPORT", "reports", "system");
        return reportGenerator.generateSystemReport();
    }

    public String marksStats(User actor) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN, UserRole.TEACHER);
        auditLogger.log(actor.getLogin(), "MARKS_STATS_REPORT", "reports", "marks_stats");
        return reportGenerator.generateMarksStatistics();
    }

    public String studentReport(User actor, Student student) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN, UserRole.TEACHER, UserRole.STUDENT);
        if (actor.getRole() == UserRole.STUDENT && !actor.getId().equals(student.getId())) {
            throw new SecurityException("Student can view only own report");
        }
        auditLogger.log(actor.getLogin(), "STUDENT_REPORT", "reports", student.getLogin());
        return reportGenerator.generateStudentReport(student);
    }
}
