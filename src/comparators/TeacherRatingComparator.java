package comparators;

import java.util.Comparator;

import users.Teacher;

public class TeacherRatingComparator implements Comparator<Teacher> {
    @Override
    public int compare(Teacher o1, Teacher o2) {
        return Double.compare(o2.getAverageRating(), o1.getAverageRating());
    }
}
