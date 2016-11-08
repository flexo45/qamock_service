package org.qamock.service;

import org.qamock.domain.*;
import org.qamock.dynamic.DynamicResourceException;
import org.qamock.dynamic.domain.DynamicResourceRequest;

import java.util.List;

public interface DynamicResourcesService {

    void receiveDynamicResourceRequest(DynamicResourceRequest resourceRequest) throws DynamicResourceException;

    DynamicResource getResource(String path);

    DynamicResource getResource(long id);

    List<DynamicResourceMethod> getAcceptanceMethods(long resourceId);

    List<DynamicResource> getResourceList();

    List<DynamicResponse> getResponseListOfResource(long resourceId);

    List<Header> getHeadersOfResponse(long responseId);

    Content getContentOfResponse(long responseId);

    Script getResourceScript(long resourceId);

    Script getResponseScript(long responseId);

    void updateResource(DynamicResource resource);

    void addTest();

}
