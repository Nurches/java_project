package research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotResearcherException;

/**
 * Represents a research project with a topic, participants, and published papers.
 */
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

    /**
     * Adds a participant to the project if they satisfy researcher requirements.
     *
     * @param participant the participant candidate
     * @throws NotResearcherException if the participant is not an active researcher
     */
    public void addParticipant(Object participant) throws NotResearcherException {
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
