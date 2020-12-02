package org.qamock.service;

import org.qamock.api.json.UserObject;
import org.qamock.domain.Role;
import org.qamock.domain.User;

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
