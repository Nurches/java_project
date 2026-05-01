package comparators;

import java.util.Comparator;

import research.ResearchPaper;

public class ResearchPaperPagesComparator implements Comparator<ResearchPaper> {
    @Override
    public int compare(ResearchPaper o1, ResearchPaper o2) {
        return Integer.compare(o1.getPages(), o2.getPages());
    }
}
