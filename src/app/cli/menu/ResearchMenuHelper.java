package app.cli.menu;

import java.time.LocalDate;
import java.util.List;

import app.cli.CliIO;
import app.cli.SessionContext;
import research.ResearchPaper;
import research.Researcher;

public final class ResearchMenuHelper {
    private ResearchMenuHelper() {
    }

    public static ResearchPaper createPaper(CliIO io, SessionContext session, Researcher researcher) {
        try {
            String title = io.readLine("Title: ");
            String authorsRaw = io.readLine("Authors (comma-separated): ");
            String journal = io.readLine("Journal: ");
            String school = io.readLine("School: ");
            int pages = io.readInt("Pages: ");
            int citations = io.readInt("Citations: ");
            String doi = io.readLine("DOI: ");
            String dateRaw = io.readLine("Published date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(dateRaw);
            return session.services().researchService.addPaper(session.currentUser(), researcher, title,
                    app.service.ResearchService.parseAuthors(authorsRaw), journal, school, pages, citations, doi,
                    date);
        } catch (Exception e) {
            io.println("Failed: " + e.getMessage());
            return null;
        }
    }

    public static void researchSubmenu(CliIO io, SessionContext session, Researcher researcher) {
        int choice = io.chooseMenu("Research", List.of(
                "My papers (by citations)",
                "My papers (by date)",
                "Add paper",
                "h-index & citations",
                "Subscribe to journal",
                "Citation format"));
        try {
            switch (choice) {
                case 1 -> session.services().researchService.printResearcherPapers(researcher, "citations");
                case 2 -> session.services().researchService.printResearcherPapers(researcher, "date");
                case 3 -> createPaper(io, session, researcher);
                case 4 -> {
                    io.println("h-index: " + researcher.calculateHIndex());
                    io.println("Total citations: " + researcher.getTotalCitations());
                }
                case 5 -> session.services().researchService.subscribeJournal(session.currentUser());
                case 6 -> citationFormat(io, session, researcher);
                default -> {
                }
            }
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
        if (choice > 0) {
            io.pause();
        }
    }

    private static void citationFormat(CliIO io, SessionContext session, Researcher researcher) {
        List<ResearchPaper> papers = researcher.getResearchPapers();
        if (papers.isEmpty()) {
            io.println("No papers.");
            return;
        }
        ResearchPaper paper = io.chooseFromList("Select paper for citation", papers, ResearchPaper::getTitle);
        if (paper == null) {
            return;
        }
        String formatRaw = io.readLine("Format (PLAIN/APA/MLA): ").trim().toUpperCase();
        if (formatRaw.isEmpty()) {
            formatRaw = "PLAIN";
        }
        io.println(session.services().researchService.citation(paper,
                enums.CitationFormat.valueOf(formatRaw)));
    }
}
