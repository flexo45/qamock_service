package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "login", unique = true, updatable = false)
    private String login;
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "password", nullable = false)
    private String password;
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Column(name = "email", unique = true, nullable = false)
    private String email;
    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    @Column(name = "role", nullable = false)
    private String role;
    public String getRole() { return  this.role; }
    public void setRole(String role) { this.role = role; }

    @Column(name = "active", nullable = false)
    private boolean active;
    public boolean getActive(){return this.active;}
    public void setActive(boolean active){this.active = active;}

    public User() {
    }

    public User(long id, String login, String password, String email, String role, boolean active) {
        this.setId(id);
        this.setLogin(login);
        this.setPassword(password);
        this.setEmail(email);
        this.setRole(role);
        this.setActive(active);
    }

    public User(String login, String password, String email, String role, boolean active) {
        this.setId(-1);
        this.setLogin(login);
        this.setPassword(password);
        this.setEmail(email);
        this.setRole(role);
        this.setActive(active);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id +
                ", login=" + login +
                ", role=" + role +
                "}";
    }
}
