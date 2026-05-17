package communication;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import enums.EmployeeRequestStatus;
import users.Employee;

public class EmployeeRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final Employee requester;
    private final String subject;
    private final String description;
    private final LocalDateTime createdAt;
    private EmployeeRequestStatus status;
    private boolean signedByDean;
    private boolean signedByRector;
    private String rejectionReason;

    public EmployeeRequest(Employee requester, String subject, String description) {
        if (requester == null) {
            throw new IllegalArgumentException("Requester is required");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject is required");
        }
        this.id = UUID.randomUUID().toString();
        this.requester = requester;
        this.subject = subject.trim();
        this.description = description == null ? "" : description.trim();
        this.createdAt = LocalDateTime.now();
        this.status = EmployeeRequestStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public Employee getRequester() {
        return requester;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public EmployeeRequestStatus getStatus() {
        return status;
    }

    public boolean isSignedByDean() {
        return signedByDean;
    }

    public boolean isSignedByRector() {
        return signedByRector;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void signByDean() {
        if (status == EmployeeRequestStatus.REJECTED) {
            throw new IllegalStateException("Cannot sign a rejected request");
        }
        if (signedByDean) {
            throw new IllegalStateException("Request is already signed by dean");
        }
        signedByDean = true;
        refreshApprovalStatus();
    }

    public void signByRector() {
        if (status == EmployeeRequestStatus.REJECTED) {
            throw new IllegalStateException("Cannot sign a rejected request");
        }
        if (signedByRector) {
            throw new IllegalStateException("Request is already signed by rector");
        }
        signedByRector = true;
        refreshApprovalStatus();
    }

    public void reject(String reason) {
        status = EmployeeRequestStatus.REJECTED;
        rejectionReason = reason == null || reason.isBlank() ? "Rejected" : reason.trim();
    }

    private void refreshApprovalStatus() {
        if (status == EmployeeRequestStatus.REJECTED) {
            return;
        }
        if (signedByDean && signedByRector) {
            status = EmployeeRequestStatus.APPROVED;
        }
    }

    @Override
    public String toString() {
        return "EmployeeRequest{" +
                "id='" + id + '\'' +
                ", requester=" + requester.getLogin() +
                ", subject='" + subject + '\'' +
                ", status=" + status +
                ", dean=" + signedByDean +
                ", rector=" + signedByRector +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EmployeeRequest other)) {
            return false;
        }
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
