package comparators;

import java.util.Comparator;

import users.Student;

public class StudentGpaComparator implements Comparator<Student> {
    @Override
    public int compare(Student o1, Student o2) {
        return Double.compare(o2.getGpa(), o1.getGpa());
    }
}
