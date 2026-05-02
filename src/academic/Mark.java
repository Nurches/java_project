package academic;

import java.io.Serializable;

public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    private final double firstAttestation;
    private final double secondAttestation;
    private final double finalExam;

    public Mark(double firstAttestation, double secondAttestation, double finalExam) {
        validate(firstAttestation, "First attestation");
        validate(secondAttestation, "Second attestation");
        validate(finalExam, "Final exam");

        this.firstAttestation = firstAttestation;
        this.secondAttestation = secondAttestation;
        this.finalExam = finalExam;
    }

    private void validate(double value, String partName) {
        if (value < 0 || value > 40) {
            throw new IllegalArgumentException(partName + " must be in range [0, 40]");
        }
    }

    public double getFirstAttestation() {
        return firstAttestation;
    }

    public double getSecondAttestation() {
        return secondAttestation;
    }

    public double getFinalExam() {
        return finalExam;
    }

    public double getTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public boolean isPassed() {
        return getTotal() >= 50.0;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "firstAttestation=" + firstAttestation +
                ", secondAttestation=" + secondAttestation +
                ", finalExam=" + finalExam +
                ", total=" + getTotal() +
                '}';
    }
}
