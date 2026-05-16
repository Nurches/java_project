package app.repository;

import java.util.List;
import java.util.Optional;

import academic.Course;

public interface CourseRepository {
    void save(Course course);

    Optional<Course> findById(String id);

    List<Course> findAll();
}
