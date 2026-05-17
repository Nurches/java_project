package research;

import java.util.Comparator;
import java.util.List;

/**
 * Describes research-related behavior shared by users who can participate in
 * research activity.
 */
public interface Researcher {
    List<ResearchPaper> getResearchPapers();

    void addResearchPaper(ResearchPaper paper);

    /**
     * Prints research papers sorted according to the provided comparator.
     *
     * @param comparator sorting strategy for papers
     */
    void printPapers(Comparator<ResearchPaper> comparator);

    int calculateHIndex();

    int getTotalCitations();

    default boolean isResearcherActive() {
        return false;
    }
}
