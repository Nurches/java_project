package comparators;

import java.util.Comparator;

import research.ResearchPaper;

public class ResearchPaperDateComparator implements Comparator<ResearchPaper> {
    @Override
    public int compare(ResearchPaper o1, ResearchPaper o2) {
        return o2.getPublishedDate().compareTo(o1.getPublishedDate());
    }
}
