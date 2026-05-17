package app.service;

import java.util.List;
import java.util.stream.Collectors;

import app.audit.AuditLogger;
import communication.EmployeeRequest;
import core.UniversitySystem;
import enums.EmployeeRequestStatus;
import enums.UserRole;
import users.Employee;
import users.User;

public class EmployeeRequestService {
    private final RbacService rbacService;
    private final AuditLogger auditLogger;

    public EmployeeRequestService(RbacService rbacService, AuditLogger auditLogger) {
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    public EmployeeRequest submit(User actor, String subject, String description) {
        if (!(actor instanceof Employee employee)) {
            throw new IllegalStateException("Only employees can submit requests");
        }
        EmployeeRequest request = new EmployeeRequest(employee, subject, description);
        UniversitySystem.getInstance().addEmployeeRequest(request);
        auditLogger.log(actor.getLogin(), "EMPLOYEE_REQUEST_SUBMIT", "employee_requests", subject);
        return request;
    }

    public List<EmployeeRequest> myRequests(User actor) {
        if (!(actor instanceof Employee employee)) {
            throw new IllegalStateException("Only employees can view their requests");
        }
        return UniversitySystem.getInstance().getEmployeeRequests().stream()
                .filter(r -> r.getRequester().getId().equals(employee.getId()))
                .collect(Collectors.toList());
    }

    public List<EmployeeRequest> listPending(User actor) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        return UniversitySystem.getInstance().getEmployeeRequests().stream()
                .filter(r -> r.getStatus() == EmployeeRequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public void signAsDean(User actor, EmployeeRequest request) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        requirePending(request);
        request.signByDean();
        auditLogger.log(actor.getLogin(), "EMPLOYEE_REQUEST_DEAN_SIGN", "employee_requests", request.getId());
    }

    public void signAsRector(User actor, EmployeeRequest request) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        requirePending(request);
        request.signByRector();
        auditLogger.log(actor.getLogin(), "EMPLOYEE_REQUEST_RECTOR_SIGN", "employee_requests", request.getId());
    }

    public void reject(User actor, EmployeeRequest request, String reason) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        requirePending(request);
        request.reject(reason);
        auditLogger.log(actor.getLogin(), "EMPLOYEE_REQUEST_REJECT", "employee_requests", request.getId());
    }

    private void requirePending(EmployeeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request not found");
        }
        if (request.getStatus() != EmployeeRequestStatus.PENDING) {
            throw new IllegalStateException("Request is not pending");
        }
    }
}
