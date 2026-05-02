package users;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import enums.UserRole;
import research.ResearchPaper;
import research.Researcher;

public class BachelorStudent extends Student implements Researcher {
    private static final long serialVersionUID = 1L;

    private List<ResearchPaper> researchPapers;

    public BachelorStudent(String id, String login, String password, String name, String email, String major,
            int yearOfStudy) {
        super(id, login, password, name, email, major, yearOfStudy);
        this.role = UserRole.BACHELOR_STUDENT;
        this.researchPapers = new ArrayList<>();
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(researchPapers);
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        if (!researchPapers.contains(paper)) {
            researchPapers.add(paper);
        }
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(comparator);
        sorted.forEach(System.out::println);
    }

    @Override
    public int calculateHIndex() {
        List<Integer> citations = researchPapers.stream()
                .map(ResearchPaper::getCitations)
                .sorted(Comparator.reverseOrder())
                .toList();

        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }

    @Override
    public int getTotalCitations() {
        return researchPapers.stream().mapToInt(ResearchPaper::getCitations).sum();
    }
}
