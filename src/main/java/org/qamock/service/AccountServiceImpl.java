package org.qamock.service;

import org.qamock.api.json.UserObject;
import org.qamock.dao.UserDao;
import org.qamock.domain.Role;
import org.qamock.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Deprecated
    @Transactional
    @Override
    public void addAccount(User user) {
        userDao.addUser(user);
    }

    @Deprecated
    @Transactional
    @Override
    public void addRole(Role role) {
        userDao.addRole(role);
    }

    @Transactional
    @Override
    public void addAccount(UserObject userObject) {
        userDao.addUser(new User(userObject.getUsername(), passwordEncoder.encode(userObject.getPassword()), userObject.getEmail(), userObject.getEnabled()));
        for(Role role : userObject.getRoles()){
            userDao.addRole(role);
        }
    }

    @Transactional
    @Override
    public List<User> listAccount() {
        return userDao.listUser();
    }

    @Transactional
    @Override
    public List<Role> listRoles(long userId) {
        return userDao.listRole(userId);
    }

    @Transactional
    @Override
    public User getAccount(String username) {
        return userDao.getUser(username);
    }

    @Transactional
    @Override
    public User getAccount(long id) { return userDao.getUser(id); }

    @Transactional
    @Override
    public void setActive(User user, boolean isActive) {
        userDao.setActive(user, isActive);
    }
}
