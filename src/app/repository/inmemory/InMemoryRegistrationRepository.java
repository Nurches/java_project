package app.repository.inmemory;

import java.util.List;
import java.util.stream.Collectors;

import academic.CourseRegistration;
import app.repository.RegistrationRepository;
import core.UniversitySystem;
import enums.RegistrationStatus;

public class InMemoryRegistrationRepository implements RegistrationRepository {
    private final UniversitySystem system = UniversitySystem.getInstance();

    @Override
    public void save(CourseRegistration registration) {
        system.addRegistration(registration);
    }

    @Override
    public List<CourseRegistration> findAll() {
        return system.getRegistrations();
    }

    @Override
    public List<CourseRegistration> findByStatus(RegistrationStatus status) {
        return system.getRegistrations().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }
}
