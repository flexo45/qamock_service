package org.qamock.app.main.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.qamock.app.main.domain.Role;
import org.qamock.app.main.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addUser(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public void addRole(Role role) {
        sessionFactory.getCurrentSession().save(role);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUser() {
        return sessionFactory.getCurrentSession().createQuery("from User").list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> listRole(long userId) {

        User user = (User) sessionFactory.getCurrentSession().get(User.class, userId);

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Role.class);
        return criteria.add(Restrictions.eq("username", user.getUsername())).list();
    }

    @Override
    public User getUser(String username) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
        return ((User) criteria.add(Restrictions.eq("username", username)).uniqueResult());
    }

    @Override
    public User getUser(long id) {
        return (User) sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Override
    public void setActive(User user, boolean isActive) {

        user.setEnabled(isActive);
        sessionFactory.getCurrentSession().saveOrUpdate(user);

        /*Query query = sessionFactory.getCurrentSession().createQuery("update User set active = :active where id = :id");
        query.setParameter("active", isActive);
        query.setParameter("id", id);
        query.executeUpdate();*/
    }
}
