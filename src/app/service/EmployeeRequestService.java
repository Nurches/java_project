package app.service;

import java.util.List;
import java.util.stream.Collectors;

import app.audit.AuditLogger;
import communication.EmployeeRequest;
import core.UniversitySystem;
import enums.EmployeeRequestStatus;
import enums.UserRole;
import users.Employee;
import users.Manager;
import users.User;

/**
 * Handles submission, listing, signing, and rejection of employee requests.
 */
public class EmployeeRequestService {
    private final RbacService rbacService;
    private final AuditLogger auditLogger;

    public EmployeeRequestService(RbacService rbacService, AuditLogger auditLogger) {
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    /**
     * Submits a new employee request on behalf of the acting employee.
     *
     * @param actor employee submitting the request
     * @param subject request subject
     * @param description request details
     * @return created employee request
     */
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

    /**
     * Signs an employee request as dean.
     *
     * @param actor user performing the signature
     * @param request request to sign
     */
    public void signAsDean(User actor, EmployeeRequest request) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        requirePending(request);
        requireDeanSigner(actor);
        request.signByDean();
        auditLogger.log(actor.getLogin(), "EMPLOYEE_REQUEST_DEAN_SIGN", "employee_requests", request.getId());
    }

    /**
     * Signs an employee request as rector.
     *
     * @param actor user performing the signature
     * @param request request to sign
     */
    public void signAsRector(User actor, EmployeeRequest request) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        requirePending(request);
        requireRectorSigner(actor);
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

    private void requireDeanSigner(User actor) {
        if (actor.getRole() == UserRole.ADMIN) {
            return;
        }
        if (!(actor instanceof Manager manager) || !manager.canSignAsDean()) {
            throw new SecurityException("Only a dean-capable manager or admin can sign as dean");
        }
    }

    private void requireRectorSigner(User actor) {
        if (actor.getRole() == UserRole.ADMIN) {
            return;
        }
        if (!(actor instanceof Manager manager) || !manager.canSignAsRector()) {
            throw new SecurityException("Only a rector-capable manager or admin can sign as rector");
        }
    }
}
