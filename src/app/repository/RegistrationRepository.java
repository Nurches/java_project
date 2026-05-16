package app.repository;

import java.util.List;

import academic.CourseRegistration;
import enums.RegistrationStatus;

public interface RegistrationRepository {
    void save(CourseRegistration registration);

    List<CourseRegistration> findAll();

    List<CourseRegistration> findByStatus(RegistrationStatus status);
}
