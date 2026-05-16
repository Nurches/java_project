package app.repository.inmemory;

import java.util.List;
import java.util.Optional;

import app.repository.UserRepository;
import core.UniversitySystem;
import users.User;

public class InMemoryUserRepository implements UserRepository {
    private final UniversitySystem system = UniversitySystem.getInstance();

    @Override
    public void save(User user) {
        system.addUser(user);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(system.findUserByLogin(login));
    }

    @Override
    public Optional<User> findById(String id) {
        return system.getUsers().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return system.getUsers();
    }

    @Override
    public void deleteById(String id) {
        findById(id).ifPresent(system::removeUser);
    }
}
