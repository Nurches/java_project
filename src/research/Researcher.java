package research;

import java.util.Comparator;
import java.util.List;

public interface Researcher {
    List<ResearchPaper> getResearchPapers();

    void addResearchPaper(ResearchPaper paper);

    void printPapers(Comparator<ResearchPaper> comparator);

    int calculateHIndex();

    int getTotalCitations();

    default boolean isResearcherActive() {
        return false;
    }
}
