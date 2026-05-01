package reports;

import core.UniversitySystem;

public class ReportGenerator {

    public String generateSystemReport() {
        // TODO Add richer reporting logic by module (users/courses/research/communication).
        UniversitySystem system = UniversitySystem.getInstance();
        return "System report: users=" + system.getUsers().size() +
                ", courses=" + system.getCourses().size() +
                ", papers=" + system.getResearchPapers().size();
    }
}
