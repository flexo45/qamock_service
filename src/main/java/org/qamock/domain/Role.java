package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_roles")
public class Role implements Serializable {

    private static final long serialVersionUID = -8706638414326132798L;

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

    @Column(name = "username", updatable = false)
    private String username;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "role", nullable = false)
    private String role;
    public String getRole() { return  this.role; }
    public void setRole(String role) { this.role = role; }

    public Role() {
    }

    public Role(long id, String username, String password, String email, String role, boolean enabled) {
        this.setId(id);
        this.setUsername(username);
        this.setRole(role);
    }

    public Role(String username, String password, String email, String role, boolean enabled) {
        this.setId(-1);
        this.setUsername(username);
        this.setRole(role);
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id +
                ", username=" + username +
                ", role=" + role +
                "}";
    }
}
