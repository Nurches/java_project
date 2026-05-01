package comparators;

import java.util.Comparator;

import research.Researcher;

public class ResearcherCitationsComparator implements Comparator<Researcher> {
    @Override
    public int compare(Researcher o1, Researcher o2) {
        return Integer.compare(o2.getTotalCitations(), o1.getTotalCitations());
    }
}
