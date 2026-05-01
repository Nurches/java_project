package comparators;

import java.util.Comparator;

import users.Student;

public class StudentAlphabetComparator implements Comparator<Student> {
    @Override
    public int compare(Student o1, Student o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
