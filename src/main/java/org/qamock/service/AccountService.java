package org.qamock.service;

import org.qamock.domain.User;

import java.util.List;

public interface AccountService {

    void addAccount(User user);

    List<User> listAccount();

    User getAccount(String login);

    void setActive(User user, boolean isActive);

}
