package org.qamock.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.qamock.api.json.LogRow;
import org.qamock.api.json.MockRequestPojo;
import org.qamock.api.json.ResourceObject;
import org.qamock.api.json.ResponseObject;
import org.qamock.dao.DynamicResourceDao;
import org.qamock.domain.*;
import org.qamock.dynamic.AsyncLogWriter;
import org.qamock.dynamic.DynamicResourceException;
import org.qamock.dynamic.DynamicResourceRequestHandler;
import org.qamock.dynamic.domain.DynamicResourceRequest;

import org.qamock.dynamic.script.ScriptHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DynamicResourceServiceImpl implements DynamicResourcesService{

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceServiceImpl.class);

    @Autowired
    DynamicResourceDao resourceDao;

    @Autowired
    AsyncLogWriter asyncLogWriter;

    @Autowired
    ScriptHandler scriptHandler;

    @Override
    public void receiveDynamicResourceRequest(DynamicResourceRequest resourceRequest) throws DynamicResourceException{

        logger.info("Receive dynamic resource request: " + resourceRequest);

        String resourcePath = resourceRequest.path().replace("/dynamic/resource/", "");

        DynamicResource resource = getResource(resourcePath);

        if(resource != null){

            List<DynamicResourceMethod> methods = getAcceptanceMethods(resource.getId());

            if(isAcceptedRequest(methods, resourceRequest)){

                DynamicResourceRequestHandler resourceRequestHandler =
                        new DynamicResourceRequestHandler(resourceRequest, this, scriptHandler);

                resourceRequestHandler.processResourceRequest(resource);

                if(resource.getDisable_logging() != 1){
                    asyncLogWriter.loggingDynamicRequest(resourceRequest, resource);
                    logger.info("Complete!");
                }
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
    @Cacheable("resource")
    @Override
    public DynamicResource getResource(String path){
        DynamicResource resource = resourceDao.getResource(path);
        if (resource != null) return resource;

        List<DynamicResource> suitableResources = resourceDao.getAllResourcesStartWith(path);
        if (suitableResources.size() == 1) { resource = suitableResources.get(0); }
        
        if (suitableResources.size() > 1) {
            String[] pathArrayByDirs = path.split("/");
            for (DynamicResource possibleResource: suitableResources) {
                boolean isHit = false;
                String[] pathArrayByDirsOfPossibleResource = possibleResource.getPath().split("/");
                if (pathArrayByDirsOfPossibleResource.length == pathArrayByDirs.length) {
                    for (int i = 0; i < pathArrayByDirs.length; i++) {
                        if (pathArrayByDirsOfPossibleResource[i].equals("*")) {
                            isHit = true;
                        } else {
                            if (!pathArrayByDirsOfPossibleResource[i].equals(pathArrayByDirs[i])) {
                                isHit = false;
                                break;
                            }
                        }
                    }
                    if (isHit) { resource = possibleResource; }
                }
            }
        }
        return resource;
    }

    @Transactional
    @Cacheable("resource")
    @Override
    public DynamicResource getResource(long id) {
        return resourceDao.getResource(id);
    }

    @Transactional
    @Cacheable("response")
    @Override
    public DynamicResponse getResponse(long id) {
        return  resourceDao.getResponse(id);
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
    @SuppressWarnings("unchecked")
    @Override
    public List<LogRow> getResourceLog(int size, String resource) {
        List<LogRow> resourceLogs = new ArrayList<LogRow>();

        List<DynamicRequestLog> requestLogList;

        size = size <= 0 ? 10 : (size > 20 ? 20 : size);

        if(resource != null){
            DynamicResource dynamicResource = resourceDao.getResource(resource);
            if(dynamicResource != null){
                requestLogList = resourceDao.listRequestLogsByResource(dynamicResource.getId(), size);
            }
            else {
                requestLogList = resourceDao.listRequestLogs(size);
            }
        }
        else {
            requestLogList = resourceDao.listRequestLogs(size);
        }

        for(DynamicRequestLog requestLog : requestLogList){
            LogRow row = new LogRow();
            try {
                row.setId(requestLog.getId());
                row.setResource(requestLog.getDynamicResource().getPath());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd'T'hh:mm:ss:SSSz");
                row.setTimestamp(simpleDateFormat.format(requestLog.getCreated()));

                MockRequest request = resourceDao.getMockRequest(requestLog.getId());
                MockRequestPojo requestPojo = new MockRequestPojo();
                requestPojo.setId(request.getId());
                requestPojo.setMethod(request.getMethod());
                requestPojo.setPath(request.getPath());
                requestPojo.setHeaders((Map<String, String>) new ObjectMapper().readValue(request.getHeaders(), new TypeReference<Map<String, String>>(){}));
                requestPojo.setContent(request.getContent());
                row.setRequest(requestPojo);

                row.setResponse(resourceDao.getMockResponse(requestLog.getId()));

                resourceLogs.add(row);
            }
            catch (IOException ioe){
                logger.error("Error on create log row: " + row + " from request lgo: " + requestLog, ioe);
            }
        }

        return resourceLogs;
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
        DynamicResource dynamicResource = new DynamicResource("test", 0, null, null, 0);
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
        DynamicResource resource = new DynamicResource(resourceObject.getPath(), resourceObject.getStrategy(), null, null,
                resourceObject.getLogging() < 0 ? 0 : resourceObject.getLogging());
        resourceDao.addResource(resource);

        if(resourceObject.getScript() != null){
            if(!resourceObject.getScript().equals("")){
                Script script = new Script(resourceObject.getScript(), resource, null);
                resourceDao.addScript(script);
            }
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
            if(!responseObject.getContent().equals("")){
                resourceDao.addContent(new Content(responseObject.getContent(), response));
            }
        }

        if(responseObject.getScript() != null){
            if(!responseObject.getScript().equals("")){
                resourceDao.addScript(new Script(responseObject.getScript(), null, response));
            }
        }

        for(String hdr : responseObject.getHeaders()){
            try {
                String[] arr = hdr.split(":");
                if(arr.length > 1){
                    resourceDao.addHeader(new Header(arr[0], arr[1], response));
                }
                else {
                    logger.warn("Incorrect header found: " + hdr + ". Ignore");
                }
            }
            catch (Exception e){
                logger.info("Header already exist: " + hdr + ". Ignore");
            }
        }
    }

    @Transactional
    @CacheEvict(value = "resource", allEntries = true)
    @Override
    public void updateResource(ResourceObject resourceObject) {
        logger.info("Receive update resource request: " + resourceObject);
        DynamicResource resource = resourceDao.getResource(resourceObject.getId());
        resource.setPath(resourceObject.getPath());
        resource.setDispatch_strategy(resourceObject.getStrategy());
        resource.setDefaultDynamicResponse(resourceDao.getResponse(resourceObject.getDefault_resp()));
        resource.setDisable_logging(resourceObject.getLogging());
        resourceDao.updateResource(resource);

        //scrip updater
        Script script = resourceDao.resourceScript(resource.getId());
        String script_text = resourceObject.getScript() == null ? "" : resourceObject.getScript();
        if(script != null){
            script.setText(script_text);
            resourceDao.updateScript(script);
        }
        else {
            if(!script_text.equals("")){
                script = new Script(script_text, resource, null);
                resourceDao.updateScript(script);
            }
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
    @CacheEvict(value = "response", allEntries = true)
    @Override
    public void updateResponse(ResponseObject responseObject) {
        logger.info("Receive update response request: " + responseObject);
        DynamicResponse response = resourceDao.getResponse(responseObject.getId());
        response.setCode(responseObject.getCode());
        response.setName(responseObject.getName());
        resourceDao.updateResponse(response);

        //content updater
        Content content = resourceDao.responseContent(responseObject.getId());
        String content_text = responseObject.getContent() == null ? "" : responseObject.getContent();
        if(content != null){
            content.setText(content_text);
            resourceDao.updateContent(content);
        }
        else {
            if(!content_text.equals("")){
                content = new Content(content_text, response);
                resourceDao.updateContent(content);
            }
        }

        //scrip updater
        Script script = resourceDao.responseScript(responseObject.getId());
        String script_text = responseObject.getScript() == null ? "" : responseObject.getScript();
        if(script != null){
            script.setText(script_text);
            resourceDao.updateScript(script);
        }
        else {
            if(!script_text.equals("")){
                script = new Script(script_text, null, response);
                resourceDao.updateScript(script);
            }
        }

        //headers updater
        List<Header> new_headers = new ArrayList<Header>();
        List<String> headers_raw = responseObject.getHeaders() == null ? new ArrayList<String>() : responseObject.getHeaders();

        for(String i : headers_raw){
            String[] arr = i.split(":");
            if(arr.length > 1){
                new_headers.add(new Header(arr[0], arr[1], response));
            }
            else {
                logger.warn("Incorrect header found: " + i + ". Ignore");
            }
        }

        List<Header> old_headers = resourceDao.listResponseHeaders(responseObject.getId());

        List<Header> for_create = new ArrayList<Header>(new_headers);

        for(Header old_h : old_headers){
            if(!new_headers.contains(old_h)){
                logger.info("Found old header for remove: " + old_h);
                resourceDao.deleteResponseHeader(old_h);
            }
            else {
                logger.info("Found old header for update: " + old_h);
                int idx = new_headers.indexOf(old_h);
                Header new_h = new_headers.get(idx);
                logger.info("New header: " + new_h + " , idx=" + idx);
                old_h.setValue(new_h.getValue());
                resourceDao.updateResponseHeader(old_h);
                for_create.remove(new_h);
            }
        }

        for(Header new_h : for_create){
            logger.info("Found new header for create: " + new_h);
            resourceDao.addHeader(new_h);
        }
    }

    @Transactional
    @CacheEvict(value = "response", allEntries = true)
    @Override
    public void deleteResponse(long id) {
        logger.info("Receive delete response request: responseId=" + id);
        DynamicResponse response = resourceDao.getResponse(id);
        if(response != null){
            resourceDao.deleteResponse(response);
        }
    }

    @Transactional
    @CacheEvict(value = "resource", allEntries = true)
    @Override
    public void deleteResource(long id) {
        logger.info("Receive delete resource request: resourceId=" + id);
        DynamicResource resource = resourceDao.getResource(id);
        if(resource != null){
            resourceDao.deleteResource(resource);
        }
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