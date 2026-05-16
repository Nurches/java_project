package app.repository;

import java.util.List;
import java.util.Optional;

import users.User;

public interface UserRepository {
    void save(User user);

    Optional<User> findByLogin(String login);

    Optional<User> findById(String id);

    List<User> findAll();

    void deleteById(String id);
}
