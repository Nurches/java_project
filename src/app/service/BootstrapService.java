package app.service;

import academic.Course;
import core.UniversitySystem;
import enums.CourseType;
import enums.Language;
import enums.ManagerType;
import enums.TeacherTitle;
import users.Admin;
import users.BachelorStudent;
import users.Manager;
import users.MasterStudent;
import users.Teacher;

public class BootstrapService {
    public void seedIfEmpty() {
        UniversitySystem system = UniversitySystem.getInstance();
        if (!system.getUsers().isEmpty()) {
            return;
        }

        Admin admin = new Admin("A1", "admin", "admin123", "System Admin", "admin@uni.edu", 5000.0, "IT");
        admin.setPassword("admin123");
        BachelorStudent student = new BachelorStudent("S1", "student", "stud123", "Alice Student", "alice@uni.edu",
                "Computer Science", 4);
        student.setPassword("stud123");
        Teacher teacher = new Teacher("T1", "teacher", "teach123", "Dr. Brown", "brown@uni.edu", 4000.0, "CS",
                TeacherTitle.PROFESSOR);
        teacher.setPassword("teach123");
        MasterStudent master = new MasterStudent("M1", "master", "master123", "Bob Master", "bob@uni.edu",
                "Data Science", 1);
        master.setPassword("master123");
        Manager manager = new Manager("MN1", "manager", "man123", "Mia Manager", "mia@uni.edu", 4200,
                "Registrar", ManagerType.REGISTRAR);
        manager.setPassword("man123");

        BachelorStudent student2 = new BachelorStudent("S2", "student2", "stud123", "Carol Student", "carol@uni.edu",
                "Computer Science", 3);
        student2.setPassword("stud123");
        Teacher teacher2 = new Teacher("T2", "teacher2", "teach123", "Dr. Lee", "lee@uni.edu", 3800.0, "Math",
                TeacherTitle.SENIOR_LECTURER);
        teacher2.setPassword("teach123");
        Manager manager2 = new Manager("MN2", "manager2", "man123", "Max Manager", "max@uni.edu", 4100,
                "Science Office", ManagerType.SCIENCE);
        manager2.setPassword("man123");

        system.addUser(admin);
        system.addUser(student);
        system.addUser(student2);
        system.addUser(teacher);
        system.addUser(teacher2);
        system.addUser(master);
        system.addUser(manager);
        system.addUser(manager2);

        Course oop = new Course("C1", "OOP", 5, CourseType.MAJOR, Language.ENGLISH, "Computer Science", 4);
        Course db = new Course("C2", "Databases", 5, CourseType.MAJOR, Language.ENGLISH, "Computer Science", 4);
        system.addCourse(oop);
        system.addCourse(db);
        manager.assignTeacherToCourse(teacher, oop);
        manager.assignTeacherToCourse(teacher, db);
        manager.assignTeacherToCourse(teacher2, db);
    }
}
