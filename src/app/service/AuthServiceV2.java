package app.service;

import app.repository.UserRepository;
import exceptions.AuthenticationException;
import exceptions.UserNotFoundException;
import users.User;

public class AuthServiceV2 {
    private final UserRepository userRepository;

    public AuthServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String login, String password) throws AuthenticationException, UserNotFoundException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login '" + login + "' not found"));

        if (!user.authenticate(login, password)) {
            throw new AuthenticationException("Invalid credentials");
        }

        return user;
    }
}
