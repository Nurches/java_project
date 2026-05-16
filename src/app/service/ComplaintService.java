package app.service;

import java.util.List;

import app.audit.AuditLogger;
import communication.Complaint;
import core.UniversitySystem;
import enums.UrgencyLevel;
import enums.UserRole;
import users.Student;
import users.Teacher;
import users.User;

public class ComplaintService {
    private final RbacService rbacService;
    private final AuditLogger auditLogger;

    public ComplaintService(RbacService rbacService, AuditLogger auditLogger) {
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    public Complaint file(User actor, String studentLogin, String text, UrgencyLevel urgency) {
        rbacService.requireRole(actor, UserRole.TEACHER, UserRole.ADMIN);
        if (!(actor instanceof Teacher teacher)) {
            throw new SecurityException("Only teachers can file complaints");
        }
        User target = UniversitySystem.getInstance().findUserByLogin(studentLogin);
        if (!(target instanceof Student student)) {
            throw new IllegalArgumentException("Target must be a student");
        }
        Complaint complaint = teacher.sendComplaint(student, text, urgency);
        UniversitySystem.getInstance().addComplaint(complaint);
        auditLogger.log(actor.getLogin(), "FILE_COMPLAINT", "complaints", "student=" + studentLogin);
        return complaint;
    }

    public List<Complaint> listAll(User actor) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        return UniversitySystem.getInstance().getComplaints();
    }
}
