package org.qamock.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.qamock.domain.ScriptSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ScriptDaoImpl implements ScriptsDao {

    private static final Logger logger = LoggerFactory.getLogger(ScriptDaoImpl.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<ScriptSuite> list() {
        return sessionFactory.getCurrentSession().createQuery("from ScriptSuite").list();
    }

    @Override
    public ScriptSuite get(String name) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ScriptSuite.class);
        return (ScriptSuite) criteria.add(Restrictions.eq("name", name)).uniqueResult();
    }

    @Override
    public ScriptSuite get(long id) {
        return (ScriptSuite) sessionFactory.getCurrentSession().get(ScriptSuite.class, id);
    }

    @Override
    public void add(ScriptSuite scriptSuite) {
        sessionFactory.getCurrentSession().save(scriptSuite);
    }

    @Override
    public void update(ScriptSuite scriptSuite) {
        sessionFactory.getCurrentSession().merge(scriptSuite);
    }

    @Override
    public void delete(ScriptSuite scriptSuite) {
        sessionFactory.getCurrentSession().delete(scriptSuite);
    }
}
