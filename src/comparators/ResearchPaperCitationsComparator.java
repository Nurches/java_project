package comparators;

import java.util.Comparator;

import research.ResearchPaper;

public class ResearchPaperCitationsComparator implements Comparator<ResearchPaper> {
    @Override
    public int compare(ResearchPaper o1, ResearchPaper o2) {
        return Integer.compare(o2.getCitations(), o1.getCitations());
    }
}
