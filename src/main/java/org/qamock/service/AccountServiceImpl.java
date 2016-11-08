package org.qamock.service;

import org.qamock.dao.UserDao;
import org.qamock.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public void addAccount(User user) {
        userDao.addUser(user);
    }

    @Transactional
    @Override
    public List<User> listAccount() {
        return userDao.listUser();
    }

    @Transactional
    @Override
    public User getAccount(String login) {
        return userDao.getUser(login);
    }

    @Transactional
    @Override
    public void setActive(User user, boolean isActive) {
        userDao.setActive(user, isActive);
    }
}
