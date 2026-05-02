package research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotResearcherException;
import users.Teacher;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String topic;
    private List<ResearchPaper> publishedPapers;
    private List<Researcher> participants;

    public ResearchProject(String topic) {
        this.topic = topic;
        this.publishedPapers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    public String getTopic() {
        return topic;
    }

    public List<ResearchPaper> getPublishedPapers() {
        return new ArrayList<>(publishedPapers);
    }

    public List<Researcher> getParticipants() {
        return new ArrayList<>(participants);
    }

    public void addParticipant(Object participant) throws NotResearcherException {
        if (participant instanceof Teacher teacher && !teacher.isResearcherActive()) {
            teacher.becomeResearcher();
        }

        if (!(participant instanceof Researcher researcher)) {
            throw new NotResearcherException("Participant is not a researcher");
        }
        if (!researcher.isResearcherActive()) {
            throw new NotResearcherException("Participant is not an active researcher");
        }
        if (!participants.contains(researcher)) {
            participants.add(researcher);
        }
    }

    public void addPaper(ResearchPaper paper) {
        if (!publishedPapers.contains(paper)) {
            publishedPapers.add(paper);
        }
    }
}
