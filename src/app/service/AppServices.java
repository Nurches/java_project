package app.service;

import app.audit.AuditLogger;
import app.repository.CourseRepository;
import app.repository.RegistrationRepository;
import app.repository.UserRepository;
import app.repository.inmemory.InMemoryCourseRepository;
import app.repository.inmemory.InMemoryRegistrationRepository;
import app.repository.inmemory.InMemoryUserRepository;

public class AppServices {
    public final UserRepository userRepository = new InMemoryUserRepository();
    public final CourseRepository courseRepository = new InMemoryCourseRepository();
    public final RegistrationRepository registrationRepository = new InMemoryRegistrationRepository();
    public final RbacService rbacService = new RbacService();
    public final AuditLogger auditLogger = new AuditLogger();

    public final AuthServiceV2 authService;
    public final RegistrationService registrationService;
    public final GradingService gradingService;
    public final NewsService newsService;
    public final ReportingService reportingService;
    public final UserAdminService userAdminService;
    public final CourseAdminService courseAdminService;
    public final MessageService messageService;
    public final ComplaintService complaintService;
    public final CommentService commentService;
    public final ResearchService researchService;
    public final StudentProfileService studentProfileService;
    public final EmployeeRequestService employeeRequestService;

    public AppServices() {
        authService = new AuthServiceV2(userRepository);
        registrationService = new RegistrationService(courseRepository, registrationRepository, rbacService,
                auditLogger);
        gradingService = new GradingService(rbacService, auditLogger);
        newsService = new NewsService(rbacService, auditLogger);
        reportingService = new ReportingService(rbacService, auditLogger);
        userAdminService = new UserAdminService(userRepository, rbacService, auditLogger);
        courseAdminService = new CourseAdminService(courseRepository, rbacService, auditLogger);
        messageService = new MessageService(auditLogger);
        complaintService = new ComplaintService(rbacService, auditLogger);
        commentService = new CommentService(auditLogger);
        researchService = new ResearchService(rbacService, auditLogger);
        studentProfileService = new StudentProfileService(courseRepository, rbacService, auditLogger);
        employeeRequestService = new EmployeeRequestService(rbacService, auditLogger);
    }
}
