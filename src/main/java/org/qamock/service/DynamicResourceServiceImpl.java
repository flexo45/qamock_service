package org.qamock.service;

import org.hibernate.HibernateException;
import org.qamock.api.json.ResourceObject;
import org.qamock.api.json.ResponseObject;
import org.qamock.dao.DynamicResourceDao;
import org.qamock.dao.DynamicResourceDaoImpl;
import org.qamock.domain.*;
import org.qamock.dynamic.DynamicResourceException;
import org.qamock.dynamic.DynamicResourceRequestHandler;
import org.qamock.dynamic.domain.DynamicResourceRequest;
import org.qamock.dynamic.DynamicResourceRequestQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class DynamicResourceServiceImpl implements DynamicResourcesService{

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceServiceImpl.class);

    @Autowired
    DynamicResourceRequestQueue requestQueue;

    @Autowired
    DynamicResourceDao resourceDao;

    @Override
    public void receiveDynamicResourceRequest(DynamicResourceRequest resourceRequest) throws DynamicResourceException{

        logger.info("Receive dynamic resource request: " + resourceRequest);

        String resourcePath = resourceRequest.path().replace("/dynamic/", "");

        DynamicResource resource = getResource(resourcePath);

        if(resource != null){

            List<DynamicResourceMethod> methods = getAcceptanceMethods(resource.getId());

            if(isAcceptedRequest(methods, resourceRequest)){

                DynamicResourceRequestHandler resourceRequestHandler =
                        new DynamicResourceRequestHandler(resourceRequest, this);

                resourceRequestHandler.processResourceRequest(resource);

            }
            else {
                resourceRequest.response().setStatus(405);
                logger.info("Method not allowed: " + resourceRequest.toString());
            }
        }
        else{
            resourceRequest.response().setStatus(404);
            logger.info("Resource resourcePath=" + resourcePath + " not found, response status=404");
        }
    }

    @Transactional
    @Override
    public DynamicResource getResource(String path){
        return resourceDao.getResource(path);
    }

    @Transactional
    @Override
    public DynamicResource getResource(long id) {
        return resourceDao.getResource(id);
    }

    @Transactional
    @Override
    public List<DynamicResourceMethod> getAcceptanceMethods(long resourceId){
        return resourceDao.listResourceMethods(resourceId);
    }

    @Override
    @Transactional
    public List<DynamicResource> getResourceList() {
        return resourceDao.listResource();
    }

    @Transactional
    @Override
    public List<DynamicResponse> getResponseListOfResource(long resourceId){
        return resourceDao.listDynamicResponses(resourceId);
    }

    @Transactional
    @Override
    public List<Header> getHeadersOfResponse(long responseId){
        return resourceDao.listResponseHeaders(responseId);
    }

    @Transactional
    @Override
    public Content getContentOfResponse(long responseId){
        return resourceDao.responseContent(responseId);
    }

    @Transactional
    @Override
    public Script getResourceScript(long resourceId) {
        return resourceDao.resourceScript(resourceId);
    }

    @Transactional
    @Override
    public Script getResponseScript(long responseId) {
        return resourceDao.responseScript(responseId);
    }

    @Override
    public void updateResource(DynamicResource resource) {
        resourceDao.updateResource(resource);
    }

    @Transactional
    @Override
        public void addTest(){
        DynamicResource dynamicResource = new DynamicResource("test", 0, null, null);
        resourceDao.addResource(dynamicResource);
        DynamicResourceMethod resourceMethod = new DynamicResourceMethod(dynamicResource, "POST");
        resourceDao.addResourceMethod(resourceMethod);
        DynamicResponse ok_response = new DynamicResponse("success", 200, dynamicResource);
        Content ok_content = new Content("{\"code\":200,\"text\":\"OK\"}", ok_response);
        resourceDao.addResponse(ok_response);
        resourceDao.addContent(ok_content);
        DynamicResponse err_response = new DynamicResponse("error", 500, dynamicResource);
        Content err_content = new Content("{\"code\":500,\"text\":\"INTERNAL SERVER ERROR\"}", err_response);
        resourceDao.addResponse(err_response);
        resourceDao.addContent(err_content);
    }

    @Transactional
    @Override
    public void createResource(ResourceObject resourceObject) {
        logger.info("Receive create resource request: " + resourceObject);
        DynamicResource resource = new DynamicResource(resourceObject.getPath(), resourceObject.getStrategy(), null, null);
        resourceDao.addResource(resource);

        if(resourceObject.getScript() != null){
            Script script = new Script(resourceObject.getScript(), resource, null);
            resourceDao.addScript(script);
        }

        for(String method : resourceObject.getMethods()){
            DynamicResourceMethod resourceMethod = new DynamicResourceMethod(resource, method);
            resourceDao.addResourceMethod(resourceMethod);
        }
    }

    @Transactional
    @Override
    public void createResponse(ResponseObject responseObject) {
        logger.info("Receive create response request: " + responseObject);
        DynamicResponse response = new DynamicResponse(responseObject.getName(), responseObject.getCode(), resourceDao.getResource(responseObject.getResource_id()));
        resourceDao.addResponse(response);

        if(responseObject.getContent() != null){
            resourceDao.addContent(new Content(responseObject.getContent(), response));
        }

        if(responseObject.getScript() != null){
            resourceDao.addScript(new Script(responseObject.getScript(), null, response));
        }

        for(Map.Entry<String, String> hdr : responseObject.getHeaders().entrySet()){
            try {
                resourceDao.addHeader(new Header(hdr.getKey(), hdr.getValue(), response));
            }
            catch (HibernateException e){
                logger.info("Header already exist: " + hdr + ". Ignore");
            }
        }
    }

    @Transactional
    @Override
    public void updateResource(ResourceObject resourceObject) {
        logger.info("Receive update resource request: " + resourceObject);
        DynamicResource resource = resourceDao.getResource(resourceObject.getId());
        resource.setPath(resourceObject.getPath());
        resource.setDispatch_strategy(resourceObject.getStrategy());
        resource.setDefaultDynamicResponse(resourceDao.getResponse(resourceObject.getDefault_resp()));
        resourceDao.updateResource(resource);

        if(resourceObject.getScript() != null){
            Script script = resourceDao.resourceScript(resourceObject.getId());
            script.setText(resourceObject.getScript());
            resourceDao.updateScript(script);
        }

        List<DynamicResourceMethod> db_list = resourceDao.listResourceMethods(resourceObject.getId());
        List<String> new_list = resourceObject.getMethods();

        for(DynamicResourceMethod db_m : db_list){
            if(!new_list.contains(db_m.getMethod())){
                resourceDao.deleteResourceMethod(db_m);
            }
            else {
                for(String m : new_list){
                    boolean exist = false;
                    for(DynamicResourceMethod i : db_list){
                        if(m.equals(i.getMethod())){exist = true;}
                    }
                    if(!exist){resourceDao.addResourceMethod(new DynamicResourceMethod(resource, m));}
                }
            }
        }
    }

    @Transactional
    @Override
    public void updateResponse(ResponseObject responseObject) {
        DynamicResponse response = resourceDao.getResponse(responseObject.getId());
        response.setCode(responseObject.getCode());
        response.setName(responseObject.getName());
        resourceDao.updateResponse(response);

        if(responseObject.getContent() != null){
            Content content = resourceDao.responseContent(responseObject.getId());
            content.setText(responseObject.getContent());
            resourceDao.updateContent(content);
        }

        if(responseObject.getScript() != null){
            Script script = resourceDao.responseScript(responseObject.getId());
            script.setText(responseObject.getScript());
            resourceDao.updateScript(script);
        }
    }

    @Transactional
    @Override
    public void deleteResponse(long id) {
        //TODO
    }

    @Transactional
    @Override
    public void deleteResource(long id) {
        logger.info("Receive delete resource request: resourceId=" + id);
        //TODO
    }

    private static boolean isAcceptedRequest(List<DynamicResourceMethod> methods, DynamicResourceRequest resourceRequest){
        boolean result = false;
        for(DynamicResourceMethod method : methods){
            if(resourceRequest.method().equals(method.getMethod())){
                result = true;
            }
        }
        return result;
    }
}