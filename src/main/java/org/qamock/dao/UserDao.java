package org.qamock.dao;

import org.qamock.domain.Role;
import org.qamock.domain.User;

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
