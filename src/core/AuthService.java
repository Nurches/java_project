package core;

import exceptions.AuthenticationException;
import exceptions.UserNotFoundException;
import users.User;

public class AuthService {

    public User login(String login, String password) throws AuthenticationException, UserNotFoundException {
        User user = UniversitySystem.getInstance().findUserByLogin(login);
        if (user == null) {
            throw new UserNotFoundException("User with login '" + login + "' not found");
        }

        if (!user.authenticate(login, password)) {
            throw new AuthenticationException("Invalid credentials");
        }

        return user;
    }
}
