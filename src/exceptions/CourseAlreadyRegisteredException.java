package exceptions;

public class CourseAlreadyRegisteredException extends Exception {
    public CourseAlreadyRegisteredException(String message) {
        super(message);
    }
}
