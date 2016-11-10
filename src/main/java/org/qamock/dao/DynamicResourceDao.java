package org.qamock.dao;

import org.qamock.domain.*;

import java.util.List;

public interface DynamicResourceDao {

    DynamicResource getResource(String path);

    DynamicResource getResource(long id);

    DynamicResponse getResponse(long id);

    Long addResource(DynamicResource resource);

    Long addResponse(DynamicResponse response);

    Long addContent(Content content);

    Long addHeader(Header header);

    Long addScript(Script script);

    Long addResourceMethod(DynamicResourceMethod resourceMethod);

    void updateResource(DynamicResource resource);

    void updateResponse(DynamicResponse response);

    void updateContent(Content content);

    void updateScript(Script script);

    void deleteResourceMethod(DynamicResourceMethod resourceMethod);

    List<DynamicResource> listResource();

    List<DynamicResourceMethod> listResourceMethods(long resourceId);

    List<DynamicResponse> listDynamicResponses(long resourceId);

    List<Header> listResponseHeaders(long responseId);

    Content responseContent(long responseId);

    Script responseScript(long responseId);

    Script resourceScript(long resourceId);

}
