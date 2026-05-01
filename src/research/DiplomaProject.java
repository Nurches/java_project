package research;

import java.io.Serializable;

public class DiplomaProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;

    public DiplomaProject(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
