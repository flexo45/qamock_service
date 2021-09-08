package org.qamock.app.main.service;

import org.qamock.app.main.domain.*;
import org.qamock.app.main.dynamic.domain.DynamicResourceRequest;
import org.qamock.app.main.api.json.LogRow;
import org.qamock.app.main.api.json.ResourceObject;
import org.qamock.app.main.api.json.ResponseObject;
import org.qamock.app.main.dynamic.DynamicResourceException;

import java.util.List;

public interface DynamicResourcesService {

    void receiveDynamicResourceRequest(DynamicResourceRequest resourceRequest) throws DynamicResourceException;

    DynamicResource getResource(String path);

    DynamicResource getResource(long id);

    DynamicResponse getResponse(long id);

    List<DynamicResourceMethod> getAcceptanceMethods(long resourceId);

    List<DynamicResource> getResourceList();

    List<DynamicResponse> getResponseListOfResource(long resourceId);

    List<Header> getHeadersOfResponse(long responseId);

    List<LogRow> getResourceLog(int size, String resource);

    Content getContentOfResponse(long responseId);

    Script getResourceScript(long resourceId);

    Script getResponseScript(long responseId);

    void updateResource(DynamicResource resource);

    void addTest();

    void createResource(ResourceObject resourceObject);

    void createResponse(ResponseObject responseObject);

    void updateResource(ResourceObject resourceObject);

    void updateResponse(ResponseObject responseObject);

    void deleteResponse(long id);

    void deleteResource(long id);

}
