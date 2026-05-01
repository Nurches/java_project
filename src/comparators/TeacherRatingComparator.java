package comparators;

import java.util.Comparator;

import users.Teacher;

public class TeacherRatingComparator implements Comparator<Teacher> {
    @Override
    public int compare(Teacher o1, Teacher o2) {
        // TODO Replace placeholder with real teacher rating metric.
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
