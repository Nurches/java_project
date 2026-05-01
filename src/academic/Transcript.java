package academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Transcript implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;
    private List<Mark> marks;
    private double gpa;

    public Transcript(String studentId, List<Mark> marks, double gpa) {
        this.studentId = studentId;
        this.marks = new ArrayList<>(marks);
        this.gpa = gpa;
    }

    public String getStudentId() {
        return studentId;
    }

    public List<Mark> getMarks() {
        return new ArrayList<>(marks);
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public String toString() {
        return "Transcript{" +
                "studentId='" + studentId + '\'' +
                ", gpa=" + gpa +
                ", marksCount=" + marks.size() +
                '}';
    }
}
