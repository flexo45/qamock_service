package org.qamock.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.qamock.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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
    @SuppressWarnings("unchecked")
    public List<User> listUser() {
        return sessionFactory.getCurrentSession().createQuery("from User").list();
    }

    @Override
    public User getUser(String login) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
        return ((User) criteria.add(Restrictions.eq("login", login)).uniqueResult());
    }

    @Override
    public void setActive(User user, boolean isActive) {

        user.setActive(isActive);
        sessionFactory.getCurrentSession().saveOrUpdate(user);

        /*Query query = sessionFactory.getCurrentSession().createQuery("update User set active = :active where id = :id");
        query.setParameter("active", isActive);
        query.setParameter("id", id);
        query.executeUpdate();*/
    }
}
