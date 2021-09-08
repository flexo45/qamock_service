package org.qamock.app.main.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.qamock.app.main.domain.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ConnectionDaoImpl implements ConnectionDao {

    private final static Logger logger = LoggerFactory.getLogger(ConnectionDaoImpl.class);

    @Autowired
    SessionFactory sessionFactory;


    @Override
    public void add(Connection connection) {
        sessionFactory.getCurrentSession().save(connection);
    }

    @Override
    public Connection get(String name) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Connection.class);
        return (Connection) criteria.add(Restrictions.eq("name", name)).uniqueResult();
    }

    @Override
    public Connection get(long id) {
        return (Connection) sessionFactory.getCurrentSession().get(Connection.class, id);
    }

    @Override
    public void update(Connection connection) {
        sessionFactory.getCurrentSession().merge(connection);
    }

    @Override
    public void delete(Connection connection) {
        sessionFactory.getCurrentSession().delete(connection);
    }
}
