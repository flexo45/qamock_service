package org.qamock.app.main.dao;

import org.qamock.app.main.domain.Role;
import org.qamock.app.main.domain.User;

import java.util.List;

public interface UserDao {

    void addUser(User user);

    void addRole(Role role);

    List<User> listUser();

    List<Role> listRole(long userId);

    User getUser(String login);

    User getUser(long id);

    void setActive(User user, boolean isActive);

}
