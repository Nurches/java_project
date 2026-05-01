package research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotResearcherException;

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
        return publishedPapers;
    }

    public List<Researcher> getParticipants() {
        return participants;
    }

    public void addParticipant(Object participant) throws NotResearcherException {
        if (!(participant instanceof Researcher)) {
            throw new NotResearcherException("Participant is not a researcher");
        }
        participants.add((Researcher) participant);
    }

    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
    }
}
