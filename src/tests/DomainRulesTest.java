package tests;

import academic.Course;
import academic.Mark;
import enums.CourseType;
import enums.Language;
import enums.TeacherTitle;
import exceptions.CourseAlreadyRegisteredException;
import exceptions.CreditLimitException;
import users.BachelorStudent;
import users.Teacher;

public class DomainRulesTest {
    public static void main(String[] args) throws Exception {
        testCreditLimit();
        testEligibilityAndMarking();
        testRatingBounds();
        System.out.println("DomainRulesTest passed");
    }

    private static void testCreditLimit() throws CreditLimitException, CourseAlreadyRegisteredException {
        BachelorStudent s = new BachelorStudent("S", "s", "p", "Student", "s@u", "CS", 2);
        s.registerForCourse(new Course("C1", "A", 10, CourseType.MAJOR, Language.ENGLISH));
        s.registerForCourse(new Course("C2", "B", 10, CourseType.MAJOR, Language.ENGLISH));
        boolean thrown = false;
        try {
            s.registerForCourse(new Course("C3", "C", 10, CourseType.MAJOR, Language.ENGLISH));
        } catch (CreditLimitException e) {
            thrown = true;
        }
        if (!thrown) {
            throw new IllegalStateException("Expected CreditLimitException");
        }
    }

    private static void testEligibilityAndMarking() throws Exception {
        BachelorStudent s = new BachelorStudent("S2", "s2", "p", "Student2", "s2@u", "CS", 4);
        Teacher t = new Teacher("T", "t", "p", "Teacher", "t@u", 1000, "CS", TeacherTitle.PROFESSOR);
        Course c = new Course("C4", "OOP", 5, CourseType.MAJOR, Language.ENGLISH, "CS", 4);
        if (!c.isEligible(s)) {
            throw new IllegalStateException("Expected eligibility");
        }
        t.assignCourse(c);
        s.registerForCourse(c);
        t.putMark(s, c, new Mark(30, 30, 40));
        if (s.getGpa() <= 0.0) {
            throw new IllegalStateException("Expected GPA > 0");
        }
    }

    private static void testRatingBounds() {
        BachelorStudent s = new BachelorStudent("S3", "s3", "p", "Student3", "s3@u", "CS", 1);
        Teacher t = new Teacher("T2", "t2", "p", "Teacher2", "t2@u", 1000, "CS", TeacherTitle.TUTOR);
        boolean thrown = false;
        try {
            s.rateTeacher(t, 6);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        if (!thrown) {
            throw new IllegalStateException("Expected rating bounds exception");
        }
    }
}
