package org.qamock.app.main.service;

import org.qamock.app.main.domain.Role;
import org.qamock.app.main.api.json.UserObject;
import org.qamock.app.main.domain.User;

import java.util.List;

public interface AccountService {

    void addAccount(User user);

    void addAccount(UserObject userObject);

    void addRole(Role role);

    List<User> listAccount();

    List<Role> listRoles(long userId);

    User getAccount(String login);

    User getAccount(long id);

    void setActive(User user, boolean isActive);

}
