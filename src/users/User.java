package users;

import java.io.Serializable;
import java.util.Objects;

import app.security.PasswordHasher;
import enums.UserRole;

/**
 * Base abstract user of the university system.
 * Stores identity, credentials, and role information shared by all users.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String login;
    protected String password;
    protected String name;
    protected String email;
    protected UserRole role;

    /**
     * Creates a user and hashes the provided raw password.
     *
     * @param id unique user identifier
     * @param login unique login
     * @param password raw password
     * @param name display name
     * @param email email address
     * @param role system role
     */
    protected User(String id, String login, String password, String name, String email, UserRole role) {
        this.id = id;
        this.login = login;
        this.password = PasswordHasher.hashPassword(password);
        this.name = name;
        this.email = email;
        this.role = role;
    }

    /**
     * Verifies login and password credentials for this user.
     *
     * @param login provided login
     * @param password provided raw password
     * @return {@code true} if credentials match
     */
    public boolean authenticate(String login, String password) {
        if (!this.login.equals(login)) {
            return false;
        }
        return PasswordHasher.verifyPassword(password, this.password);
    }

    /**
     * Replaces the current password with a newly hashed value.
     *
     * @param rawPassword raw password to hash and store
     */
    public void setPassword(String rawPassword) {
        this.password = PasswordHasher.hashPassword(rawPassword);
    }

    public UserRole getRole() {
        return role;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name.trim();
        }
    }

    public void setEmail(String email) {
        if (email != null && !email.isBlank()) {
            this.email = email.trim();
        }
    }

    public void setLogin(String login) {
        if (login != null && !login.isBlank()) {
            this.login = login.trim();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(id, other.id) && Objects.equals(login, other.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}
