package org.qamock.app.main.dao;

import org.qamock.app.main.domain.*;

import java.util.List;

public interface DynamicResourceDao {

    List<DynamicResource> getAllResourcesStartWith(String path);

    DynamicResource getResource(String path);

    DynamicResource getResource(long id);

    DynamicResponse getResponse(long id);

    MockRequest getMockRequest(long logId);

    MockResponse getMockResponse(long logId);

    Long addResource(DynamicResource resource);

    Long addResponse(DynamicResponse response);

    Long addContent(Content content);

    Long addHeader(Header header);

    Long addScript(Script script);

    Long addResourceMethod(DynamicResourceMethod resourceMethod);

    Long addDynamicRequestLog(DynamicRequestLog requestLog);

    Long addMockRequest(MockRequest request);

    Long addMockResponse(MockResponse response);

    void updateResource(DynamicResource resource);

    void updateResponse(DynamicResponse response);

    void updateResponseHeader(Header header);

    void updateContent(Content content);

    void updateScript(Script script);

    void deleteResource(DynamicResource resource);

    void deleteResponse(DynamicResponse response);

    void deleteScript(Script script);

    void deleteResourceMethod(DynamicResourceMethod resourceMethod);

    void deleteResponseHeader(Header header);

    void deleteDynamicRequestLogsByResource(long resourceId);

    void deleteDynamicRequestLogsAll();

    List<DynamicResource> listResource();

    List<DynamicRequestLog> listRequestLogs(int size);

    List<DynamicRequestLog> listRequestLogsByResource(long resourceId, int size);

    List<DynamicResourceMethod> listResourceMethods(long resourceId);

    List<DynamicResponse> listDynamicResponses(long resourceId);

    List<Header> listResponseHeaders(long responseId);

    Content responseContent(long responseId);

    Script responseScript(long responseId);

    Script resourceScript(long resourceId);

    Sequence getSequence(String name);

    void addSequence(Sequence sequence);

    void updateSequence(Sequence sequence);

    void deleteSequence(Sequence sequence);

}
