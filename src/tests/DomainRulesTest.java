package tests;

import academic.Course;
import academic.Mark;
import enums.CourseType;
import enums.Language;
import enums.TeacherTitle;
import exceptions.CourseAlreadyRegisteredException;
import exceptions.CreditLimitException;
import exceptions.LowHIndexException;
import exceptions.RegistrationException;
import research.ResearchPaper;
import users.BachelorStudent;
import users.Manager;
import users.Teacher;

public class DomainRulesTest {
    public static void main(String[] args) throws Exception {
        testCreditLimit();
        testEligibilityAndMarking();
        testRatingBounds();
        testBachelorSupervisorRules();
        testPrerequisites();
        testCourseCapacity();
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

    private static void testBachelorSupervisorRules() throws LowHIndexException {
        BachelorStudent year2 = new BachelorStudent("S4", "s4", "p", "Y2", "s4@u", "CS", 2);
        Teacher lowH = new Teacher("T3", "t3", "p", "Low", "t3@u", 1000, "CS", enums.TeacherTitle.TUTOR);
        lowH.becomeResearcher();
        lowH.addResearchPaper(new ResearchPaper("P", java.util.List.of("A"), "J", "CS", 5, 1, "doi", java.time.LocalDate.now()));

        boolean yearBlocked = false;
        try {
            year2.setSupervisor(lowH);
        } catch (IllegalStateException e) {
            yearBlocked = true;
        }
        if (!yearBlocked) {
            throw new IllegalStateException("Expected supervisor blocked for non-4th year");
        }

        BachelorStudent year4 = new BachelorStudent("S5", "s5", "p", "Y4", "s5@u", "CS", 4);
        boolean lowHBlocked = false;
        try {
            year4.setSupervisor(lowH);
        } catch (LowHIndexException e) {
            lowHBlocked = true;
        }
        if (!lowHBlocked) {
            throw new IllegalStateException("Expected LowHIndexException");
        }

        Teacher good = new Teacher("T4", "t4", "p", "Good", "t4@u", 1000, "CS", enums.TeacherTitle.PROFESSOR);
        good.addResearchPaper(new ResearchPaper("P1", java.util.List.of("A"), "J", "CS", 5, 10, "doi", java.time.LocalDate.now()));
        good.addResearchPaper(new ResearchPaper("P2", java.util.List.of("A"), "J", "CS", 5, 8, "doi", java.time.LocalDate.now()));
        good.addResearchPaper(new ResearchPaper("P3", java.util.List.of("A"), "J", "CS", 5, 6, "doi", java.time.LocalDate.now()));
        year4.setSupervisor(good);
        if (year4.getSupervisor() != good) {
            throw new IllegalStateException("Expected supervisor assigned");
        }
    }

    private static void testPrerequisites() throws Exception {
        BachelorStudent s = new BachelorStudent("S6", "s6", "p", "Student6", "s6@u", "CS", 4);
        Teacher t = new Teacher("T5", "t5", "p", "Teacher5", "t5@u", 1000, "CS", TeacherTitle.PROFESSOR);
        Manager m = new Manager("M1", "m1", "p", "Manager1", "m1@u", 1000, "Office", enums.ManagerType.ACADEMIC);

        Course oop1 = new Course("OOP1", "OOP-1", 5, CourseType.MAJOR, Language.ENGLISH, "CS", 4);
        Course oop2 = new Course("OOP2", "OOP-2", 5, CourseType.MAJOR, Language.ENGLISH, "CS", 4, 30,
                java.util.List.of("OOP1"));
        t.assignCourse(oop1);
        t.assignCourse(oop2);

        academic.CourseRegistration blocked = new academic.CourseRegistration(s, oop2);
        boolean prerequisiteBlocked = false;
        try {
            m.approveRegistration(blocked);
        } catch (RegistrationException e) {
            prerequisiteBlocked = true;
        }
        if (!prerequisiteBlocked) {
            throw new IllegalStateException("Expected prerequisite rejection");
        }

        s.registerForCourse(oop1);
        t.putMark(s, oop1, new Mark(30, 30, 30));
        academic.CourseRegistration allowed = new academic.CourseRegistration(s, oop2);
        m.approveRegistration(allowed);
        if (allowed.getStatus() != enums.RegistrationStatus.APPROVED) {
            throw new IllegalStateException("Expected prerequisite-approved registration");
        }
    }

    private static void testCourseCapacity() throws Exception {
        BachelorStudent s1 = new BachelorStudent("S7", "s7", "p", "Student7", "s7@u", "CS", 4);
        BachelorStudent s2 = new BachelorStudent("S8", "s8", "p", "Student8", "s8@u", "CS", 4);
        Manager m = new Manager("M2", "m2", "p", "Manager2", "m2@u", 1000, "Office", enums.ManagerType.ACADEMIC);
        Course limited = new Course("CAP1", "Limited", 5, CourseType.MAJOR, Language.ENGLISH, "CS", 4, 1,
                java.util.List.of());

        s1.registerForCourse(limited);
        academic.CourseRegistration second = new academic.CourseRegistration(s2, limited);
        boolean capacityBlocked = false;
        try {
            m.approveRegistration(second);
        } catch (RegistrationException e) {
            capacityBlocked = true;
        }
        if (!capacityBlocked) {
            throw new IllegalStateException("Expected capacity rejection");
        }
    }
}
