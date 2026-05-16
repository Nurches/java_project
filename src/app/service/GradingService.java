package app.service;

import academic.Course;
import academic.Mark;
import app.audit.AuditLogger;
import enums.UserRole;
import users.Student;
import users.Teacher;
import users.User;

public class GradingService {
    private final RbacService rbacService;
    private final AuditLogger auditLogger;

    public GradingService(RbacService rbacService, AuditLogger auditLogger) {
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    public void putMark(User actor, Teacher teacher, Student student, Course course, Mark mark) {
        rbacService.requireRole(actor, UserRole.TEACHER, UserRole.ADMIN);
        if (!actor.getId().equals(teacher.getId()) && actor.getRole() != UserRole.ADMIN) {
            throw new SecurityException("You can put marks only as yourself or as admin");
        }
        teacher.putMark(student, course, mark);
        auditLogger.log(actor.getLogin(), "PUT_MARK", "marks",
                "student=" + student.getLogin() + ",course=" + course.getId() + ",total=" + mark.getTotal());
    }
}
