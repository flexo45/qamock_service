package org.qamock.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.qamock.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class DynamicResourceDaoImpl implements DynamicResourceDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public DynamicResource getResource(String path) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DynamicResource.class);
        return (DynamicResource)criteria.add(Restrictions.eq("path", path)).uniqueResult();
    }

    @Override
    public DynamicResource getResource(long id) {
        return (DynamicResource) sessionFactory.getCurrentSession().get(DynamicResource.class, id);
    }

    @Override
    public DynamicResponse getResponse(long id) {
        return (DynamicResponse) sessionFactory.getCurrentSession().get(DynamicResponse.class, id);
    }

    @Override
    public Long addResource(DynamicResource resource) {
        return (Long) sessionFactory.getCurrentSession().save(resource);
    }

    @Override
    public Long addResponse(DynamicResponse response) {
        return (Long) sessionFactory.getCurrentSession().save(response);
    }

    @Override
    public Long addContent(Content content) {
        return (Long)sessionFactory.getCurrentSession().save(content);
    }

    @Override
    public Long addHeader(Header header) {
        return (Long) sessionFactory.getCurrentSession().save(header);
    }

    @Override
    public Long addScript(Script script) {
        return (Long) sessionFactory.getCurrentSession().save(script);
    }

    @Override
    public Long addResourceMethod(DynamicResourceMethod resourceMethod) {
        return (Long) sessionFactory.getCurrentSession().save(resourceMethod);
    }

    @Override
    public void updateResource(DynamicResource resource) {
        sessionFactory.getCurrentSession().merge(resource);
    }

    @Override
    public void updateResponse(DynamicResponse response) {
        sessionFactory.getCurrentSession().merge(response);
    }

    @Override
    public void updateContent(Content content) {
        sessionFactory.getCurrentSession().merge(content);
    }

    @Override
    public void updateScript(Script script) {
        sessionFactory.getCurrentSession().merge(script);
    }

    @Override
    public void deleteResourceMethod(DynamicResourceMethod resourceMethod) {
        sessionFactory.getCurrentSession().delete(resourceMethod);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DynamicResource> listResource() {
        return sessionFactory.getCurrentSession().createQuery("from DynamicResource").list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DynamicResourceMethod> listResourceMethods(long resourceId) {
        DynamicResource resource = (DynamicResource) sessionFactory.getCurrentSession().load(DynamicResource.class, resourceId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DynamicResourceMethod.class);
        return criteria.add(Restrictions.eq("dynamicResource", resource)).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DynamicResponse> listDynamicResponses(long resourceId) {
        DynamicResource resource = (DynamicResource) sessionFactory.getCurrentSession().load(DynamicResource.class, resourceId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DynamicResponse.class);
        return criteria.add(Restrictions.eq("dynamicResource", resource)).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Header> listResponseHeaders(long responseId) {
        DynamicResponse response = (DynamicResponse) sessionFactory.getCurrentSession().load(DynamicResponse.class, responseId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Header.class);
        return criteria.add(Restrictions.eq("dynamicResponse", response)).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Content responseContent(long responseId) {
        DynamicResponse response = (DynamicResponse) sessionFactory.getCurrentSession().load(DynamicResponse.class, responseId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Content.class);
        return (Content) criteria.add(Restrictions.eq("dynamicResponse", response)).uniqueResult();
    }

    @Override
    public Script responseScript(long responseId) {
        DynamicResponse response = (DynamicResponse) sessionFactory.getCurrentSession().load(DynamicResponse.class, responseId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Script.class);
        return (Script) criteria.add(Restrictions.eq("dynamicResponse", response)).uniqueResult();
    }

    @Override
    public Script resourceScript(long resourceId) {
        DynamicResource resource = (DynamicResource) sessionFactory.getCurrentSession().load(DynamicResource.class, resourceId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Script.class);
        return (Script) criteria.add(Restrictions.eq("dynamicResource", resource)).uniqueResult();
    }
}
