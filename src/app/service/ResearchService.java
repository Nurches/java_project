package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import app.audit.AuditLogger;
import comparators.ResearchPaperCitationsComparator;
import comparators.ResearchPaperDateComparator;
import comparators.ResearchPaperPagesComparator;
import core.UniversitySystem;
import enums.CitationFormat;
import enums.UserRole;
import exceptions.NotResearcherException;
import research.ResearchJournal;
import research.ResearchPaper;
import research.ResearchProject;
import research.Researcher;
import users.User;

/**
 * Provides research-related application services such as paper management,
 * project creation, journal subscription, and research analytics.
 */
public class ResearchService {
    private final RbacService rbacService;
    private final AuditLogger auditLogger;

    public ResearchService(RbacService rbacService, AuditLogger auditLogger) {
        this.rbacService = rbacService;
        this.auditLogger = auditLogger;
    }

    /**
     * Creates a paper, adds it to a researcher's profile, and stores it in the system.
     *
     * @param actor the user performing the action
     * @param researcher the target researcher
     * @param title paper title
     * @param authors paper authors
     * @param journal journal name
     * @param school school or research area
     * @param pages article length in pages
     * @param citations citation count
     * @param doi DOI identifier
     * @param date publication date
     * @return created research paper
     */
    public ResearchPaper addPaper(User actor, Researcher researcher, String title, List<String> authors,
            String journal, String school, int pages, int citations, String doi, LocalDate date) {
        requireResearcher(actor, researcher);
        ResearchPaper paper = new ResearchPaper(title, authors, journal, school, pages, citations, doi, date);
        researcher.addResearchPaper(paper);
        UniversitySystem.getInstance().addResearchPaper(paper);
        auditLogger.log(actor.getLogin(), "ADD_RESEARCH_PAPER", "research_papers", title);
        return paper;
    }

    public void printAllPapers(String sortBy) {
        Comparator<ResearchPaper> comparator = switch (sortBy.toLowerCase()) {
            case "date" -> new ResearchPaperDateComparator();
            case "pages" -> new ResearchPaperPagesComparator();
            default -> new ResearchPaperCitationsComparator();
        };
        UniversitySystem.getInstance().printAllResearchPapers(comparator);
    }

    public void printResearcherPapers(Researcher researcher, String sortBy) {
        Comparator<ResearchPaper> comparator = switch (sortBy.toLowerCase()) {
            case "date" -> new ResearchPaperDateComparator();
            case "pages" -> new ResearchPaperPagesComparator();
            default -> new ResearchPaperCitationsComparator();
        };
        researcher.printPapers(comparator);
    }

    public Optional<Researcher> topBySchool(String school) {
        return UniversitySystem.getInstance().getTopCitedResearcherBySchool(school);
    }

    public Optional<Researcher> topCurrentYear() {
        return UniversitySystem.getInstance().getTopCitedResearcherOfCurrentYear();
    }

    /**
     * Creates a research project with the provided topic.
     *
     * @param actor the user creating the project
     * @param topic project topic
     * @return created research project
     */
    public ResearchProject createProject(User actor, String topic) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN, UserRole.TEACHER);
        ResearchProject project = new ResearchProject(topic);
        UniversitySystem.getInstance().addResearchProject(project);
        auditLogger.log(actor.getLogin(), "CREATE_RESEARCH_PROJECT", "research_projects", topic);
        return project;
    }

    /**
     * Adds a participant to a research project.
     *
     * @param actor the user performing the action
     * @param project target project
     * @param participant participant candidate
     * @throws NotResearcherException if the participant does not satisfy project rules
     */
    public void addParticipant(User actor, ResearchProject project, Object participant) throws NotResearcherException {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN, UserRole.TEACHER);
        project.addParticipant(participant);
        auditLogger.log(actor.getLogin(), "ADD_PROJECT_PARTICIPANT", "research_projects", project.getTopic());
    }

    public void subscribeJournal(User actor) {
        ResearchJournal journal = UniversitySystem.getInstance().getResearchJournal();
        journal.subscribe(actor);
        auditLogger.log(actor.getLogin(), "JOURNAL_SUBSCRIBE", "research_journal", journal.getName());
    }

    public void publishToJournal(User actor, ResearchPaper paper) {
        rbacService.requireRole(actor, UserRole.MANAGER, UserRole.ADMIN);
        UniversitySystem.getInstance().getResearchJournal().publishPaper(paper);
        auditLogger.log(actor.getLogin(), "JOURNAL_PUBLISH", "research_journal", paper.getTitle());
    }

    public String citation(ResearchPaper paper, CitationFormat format) {
        return paper.getCitation(format);
    }

    public List<ResearchProject> listProjects() {
        return UniversitySystem.getInstance().getResearchProjects();
    }

    private void requireResearcher(User actor, Researcher researcher) {
        if (actor instanceof Researcher && researcher instanceof User targetUser && actor instanceof User actorUser) {
            if (!actorUser.getId().equals(targetUser.getId()) && actor.getRole() != UserRole.ADMIN
                    && actor.getRole() != UserRole.MANAGER) {
                throw new SecurityException("Can manage only own research profile");
            }
        } else if (actor.getRole() != UserRole.ADMIN && actor.getRole() != UserRole.MANAGER) {
            throw new SecurityException("Not allowed");
        }
    }

    public static List<String> parseAuthors(String raw) {
        List<String> authors = new ArrayList<>();
        for (String part : raw.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                authors.add(trimmed);
            }
        }
        if (authors.isEmpty()) {
            authors.add("Unknown");
        }
        return authors;
    }
}
