package org.qamock.dao;

import org.qamock.domain.User;

import java.util.List;

public interface UserDao {

    void addUser(User user);

    List<User> listUser();

    User getUser(String login);

    void setActive(User user, boolean isActive);

}
