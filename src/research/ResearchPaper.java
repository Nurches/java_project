package research;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import enums.CitationFormat;

public class ResearchPaper implements Comparable<ResearchPaper>, Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private List<String> authors;
    private String journal;
    private String school;
    private int pages;
    private int citations;
    private String doi;
    private LocalDate publishedDate;

    public ResearchPaper(String title, List<String> authors, String journal, int pages, int citations, String doi,
            LocalDate publishedDate) {
        this(title, authors, journal, "UNSPECIFIED", pages, citations, doi, publishedDate);
    }

    public ResearchPaper(String title, List<String> authors, String journal, String school, int pages, int citations,
            String doi, LocalDate publishedDate) {
        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.journal = journal;
        this.school = school;
        this.pages = pages;
        this.citations = citations;
        this.doi = doi;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return new ArrayList<>(authors);
    }

    public String getJournal() {
        return journal;
    }

    public String getSchool() {
        return school;
    }

    public int getPages() {
        return pages;
    }

    public int getCitations() {
        return citations;
    }

    public String getDoi() {
        return doi;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public String getCitation(CitationFormat format) {
        switch (format) {
            case APA:
                return String.format("%s (%d). %s. %s. DOI: %s", String.join(", ", authors),
                        publishedDate.getYear(), title, journal, doi);
            case MLA:
                return String.format("%s. \"%s.\" %s (%d): %d pages.", String.join(", ", authors), title, journal,
                        publishedDate.getYear(), pages);
            case PLAIN:
            default:
                return title + " - " + journal;
        }
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    @Override
    public String toString() {
        return "ResearchPaper{" +
                "title='" + title + '\'' +
                ", school='" + school + '\'' +
                ", journal='" + journal + '\'' +
                ", citations=" + citations +
                ", publishedDate=" + publishedDate +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ResearchPaper)) {
            return false;
        }
        ResearchPaper other = (ResearchPaper) obj;
        return Objects.equals(doi, other.doi) && Objects.equals(title, other.title);
    }
}
