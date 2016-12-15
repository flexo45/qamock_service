package org.qamock.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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
    public MockRequest getMockRequest(long logId) {
        DynamicRequestLog log = (DynamicRequestLog) sessionFactory.getCurrentSession().load(DynamicRequestLog.class, logId);
        return (MockRequest) sessionFactory.getCurrentSession().load(MockRequest.class, log.getMockRequest().getId());
    }

    @Override
    public MockResponse getMockResponse(long logId) {
        DynamicRequestLog log = (DynamicRequestLog) sessionFactory.getCurrentSession().load(DynamicRequestLog.class, logId);
        return (MockResponse) sessionFactory.getCurrentSession().load(MockResponse.class, log.getMockResponse().getId());
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
    public Long addDynamicRequestLog(DynamicRequestLog requestLog) {
        return (Long) sessionFactory.getCurrentSession().save(requestLog);
    }

    @Override
    public Long addMockRequest(MockRequest request) {
        return (Long) sessionFactory.getCurrentSession().save(request);
    }

    @Override
    public Long addMockResponse(MockResponse response) {
        return (Long) sessionFactory.getCurrentSession().save(response);
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
    public void updateResponseHeader(Header header) {
        sessionFactory.getCurrentSession().merge(header);
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
    public void deleteResource(DynamicResource resource) {

        DynamicResource dynamicResource =
                (DynamicResource) sessionFactory.getCurrentSession().get(DynamicResource.class, resource.getId());

        dynamicResource.setDefaultDynamicResponse(null);

        dynamicResource.setLastDynamicResponse(null);

        deleteDynamicRequestLogsByResource(dynamicResource.getId());

        for(DynamicResourceMethod method : listResourceMethods(dynamicResource.getId())){
            deleteResourceMethod(method);
        }

        for(DynamicResponse response : listDynamicResponses(dynamicResource.getId())){
            deleteResponse(response);
        }

        Script script = resourceScript(dynamicResource.getId());
        if(script != null){
            deleteScript(script);
        }

        sessionFactory.getCurrentSession().delete(dynamicResource);
    }

    @Override
    public void deleteResponse(DynamicResponse response) {

        for(Header header : listResponseHeaders(response.getId())){
            deleteResponseHeader(header);
        }

        Script script = responseScript(response.getId());
        if(script != null){
            deleteScript(script);
        }

        Content content = responseContent(response.getId());
        if(content != null){
            sessionFactory.getCurrentSession().delete(content);
        }

        sessionFactory.getCurrentSession().delete(response);
    }

    @Override
    public void deleteScript(Script script) {
        sessionFactory.getCurrentSession().delete(script);
    }

    @Override
    public void deleteResourceMethod(DynamicResourceMethod resourceMethod) {
        sessionFactory.getCurrentSession().delete(resourceMethod);
    }

    @Override
    public void deleteResponseHeader(Header header) {
        sessionFactory.getCurrentSession().delete(header);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteDynamicRequestLogsByResource(long resourceId) {
        DynamicResource resource =
                (DynamicResource) sessionFactory.getCurrentSession().load(DynamicResource.class, resourceId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DynamicRequestLog.class);
        List<DynamicRequestLog> requestLogList = criteria.add(Restrictions.eq("dynamicResource", resource)).list();

        for (DynamicRequestLog requestLog : requestLogList){
            MockRequest mockRequest = (MockRequest) sessionFactory.getCurrentSession()
                    .load(MockRequest.class, requestLog.getMockRequest().getId());
            MockResponse mockResponse = (MockResponse) sessionFactory.getCurrentSession()
                    .load(MockResponse.class, requestLog.getMockResponse().getId());

            sessionFactory.getCurrentSession().delete(mockRequest);
            sessionFactory.getCurrentSession().delete(mockResponse);
            sessionFactory.getCurrentSession().delete(requestLog);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteDynamicRequestLogsAll() {
        for(DynamicRequestLog requestLog : (List<DynamicRequestLog>) sessionFactory.getCurrentSession()
                .createQuery("from DynamicRequestLog").list()){

            MockRequest mockRequest = (MockRequest) sessionFactory.getCurrentSession()
                    .load(MockRequest.class, requestLog.getMockRequest().getId());
            MockResponse mockResponse = (MockResponse) sessionFactory.getCurrentSession()
                    .load(MockResponse.class, requestLog.getMockResponse().getId());

            sessionFactory.getCurrentSession().delete(mockRequest);
            sessionFactory.getCurrentSession().delete(mockResponse);
            sessionFactory.getCurrentSession().delete(requestLog);

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DynamicResource> listResource() {
        return sessionFactory.getCurrentSession().createQuery("from DynamicResource").list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DynamicRequestLog> listRequestLogs(int size) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(DynamicRequestLog.class)
                .addOrder(Order.desc("id"))
                .setMaxResults(size);
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DynamicRequestLog> listRequestLogsByResource(long resourceId, int size) {
        DynamicResource resource =
                (DynamicResource) sessionFactory.getCurrentSession().load(DynamicResource.class, resourceId);
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(DynamicRequestLog.class)
                .addOrder(Order.desc("id"))
                .setMaxResults(size);
        return criteria.add(Restrictions.eq("dynamicResponse", resource)).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DynamicResourceMethod> listResourceMethods(long resourceId) {
        DynamicResource resource =
                (DynamicResource) sessionFactory.getCurrentSession().load(DynamicResource.class, resourceId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DynamicResourceMethod.class);
        return criteria.add(Restrictions.eq("dynamicResource", resource)).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DynamicResponse> listDynamicResponses(long resourceId) {
        DynamicResource resource =
                (DynamicResource) sessionFactory.getCurrentSession().load(DynamicResource.class, resourceId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DynamicResponse.class);
        return criteria.add(Restrictions.eq("dynamicResource", resource)).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Header> listResponseHeaders(long responseId) {
        DynamicResponse response =
                (DynamicResponse) sessionFactory.getCurrentSession().load(DynamicResponse.class, responseId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Header.class);
        return criteria.add(Restrictions.eq("dynamicResponse", response)).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Content responseContent(long responseId) {
        DynamicResponse response =
                (DynamicResponse) sessionFactory.getCurrentSession().load(DynamicResponse.class, responseId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Content.class);
        return (Content) criteria.add(Restrictions.eq("dynamicResponse", response)).uniqueResult();
    }

    @Override
    public Script responseScript(long responseId) {
        DynamicResponse response =
                (DynamicResponse) sessionFactory.getCurrentSession().load(DynamicResponse.class, responseId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Script.class);
        return (Script) criteria.add(Restrictions.eq("dynamicResponse", response)).uniqueResult();
    }

    @Override
    public Script resourceScript(long resourceId) {
        DynamicResource resource =
                (DynamicResource) sessionFactory.getCurrentSession().load(DynamicResource.class, resourceId);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Script.class);
        return (Script) criteria.add(Restrictions.eq("dynamicResource", resource)).uniqueResult();
    }

    @Override
    public Sequence getSequence(String name) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sequence.class);
        return (Sequence) criteria.add(Restrictions.eq("name", name)).uniqueResult();
    }

    @Override
    public void addSequence(Sequence sequence) {
        sessionFactory.getCurrentSession().save(sequence);
    }

    @Override
    public void updateSequence(Sequence sequence) {
        sessionFactory.getCurrentSession().merge(sequence);
    }

    @Override
    public void deleteSequence(Sequence sequence) {
        sessionFactory.getCurrentSession().delete(sequence);
    }
}
