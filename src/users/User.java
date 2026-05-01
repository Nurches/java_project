package users;

import java.io.Serializable;
import java.util.Objects;

import enums.UserRole;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String login;
    protected String password;
    protected String name;
    protected String email;
    protected UserRole role;

    protected User(String id, String login, String password, String name, String email, UserRole role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public boolean authenticate(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
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
