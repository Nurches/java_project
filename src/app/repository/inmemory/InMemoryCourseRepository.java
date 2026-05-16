package app.repository.inmemory;

import java.util.List;
import java.util.Optional;

import academic.Course;
import app.repository.CourseRepository;
import core.UniversitySystem;

public class InMemoryCourseRepository implements CourseRepository {
    private final UniversitySystem system = UniversitySystem.getInstance();

    @Override
    public void save(Course course) {
        system.addCourse(course);
    }

    @Override
    public Optional<Course> findById(String id) {
        return system.getCourses().stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public List<Course> findAll() {
        return system.getCourses();
    }
}
