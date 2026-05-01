package comparators;

import java.util.Comparator;

import academic.Course;

public class CourseNameComparator implements Comparator<Course> {
    @Override
    public int compare(Course o1, Course o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
