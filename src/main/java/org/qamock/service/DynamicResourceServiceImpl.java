package org.qamock.service;

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
