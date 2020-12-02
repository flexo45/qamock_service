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

    @Column(name = "username", unique = true, updatable = false)
    private String username;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false)
    private String password;
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Column(name = "email", unique = true, nullable = false)
    private String email;
    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    public boolean getEnabled(){return this.enabled;}
    public void setEnabled(boolean enabled){this.enabled = enabled;}

    public User() {
    }

    public User(long id, String username, String password, String email, boolean enabled) {
        this.setId(id);
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setEnabled(enabled);
    }

    public User(String username, String password, String email, boolean enabled) {
        this.setId(-1);
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setEnabled(enabled);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id +
                ", login=" + username +
                ", role=" +
                "}";
    }
}
