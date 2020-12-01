package org.qamock.dynamic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.qamock.dao.DynamicResourceDao;
import org.qamock.domain.*;
import org.qamock.dynamic.domain.DynamicResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncLogWriterImpl implements AsyncLogWriter {

    private static final Logger logger = LoggerFactory.getLogger(AsyncLogWriterImpl.class);

    @Autowired
    private DynamicResourceDao resourceDao;

    @Qualifier("taskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public void loggingDynamicRequest(DynamicResourceRequest dynamicResourceRequest, DynamicResource dynamicResource) {
        taskExecutor.execute(new WriteLogTask(dynamicResourceRequest, dynamicResource));
    }

    private class WriteLogTask implements Runnable{

        private DynamicResourceRequest dynamicResourceRequest;

        private DynamicResource dynamicResource;

        public WriteLogTask(DynamicResourceRequest dynamicResourceRequest, DynamicResource dynamicResource){
            this.dynamicResourceRequest = dynamicResourceRequest;
            this.dynamicResource = dynamicResource;
        }

        @Override
        @Transactional
        public void run() {

            MockRequest mockRequest = new MockRequest();
            MockResponse mockResponse = new MockResponse();

            try {
                mockRequest.setMethod(dynamicResourceRequest.method());
                mockRequest.setPath(dynamicResourceRequest.path() +
                        (dynamicResourceRequest.query() == null ? "" : "?" + dynamicResourceRequest.query()));
                mockRequest.setContent(dynamicResourceRequest.content());
                mockRequest.setHeaders(new ObjectMapper().writeValueAsString(dynamicResourceRequest.headers()));
            }
            catch (JsonProcessingException jpe){
                logger.error("Error on parsing request headers " + dynamicResourceRequest, jpe);
            }

            /*
            try {
                mockResponse.setContent(dynamicResourceRequest.getResponseContent());
                Map<String, String> resp_headers = new HashMap<String, String>();
                Collection<String> h_names = dynamicResourceRequest.response().getHeaderNames();
                for(String hdr_name : h_names){
                    resp_headers.put(hdr_name, dynamicResourceRequest.response().getHeader(hdr_name));
                }
                mockResponse.setHeaders(new ObjectMapper().writeValueAsString(resp_headers));
            }
            catch (JsonProcessingException jpe){
                logger.error("Error on parsing response headers " + dynamicResourceRequest, jpe);
            }*/

            mockResponse.setContent(dynamicResourceRequest.getResponseContent());

            DynamicRequestLog requestLog = new DynamicRequestLog(dynamicResource, mockResponse, mockRequest);

            resourceDao.addMockRequest(mockRequest);
            resourceDao.addMockResponse(mockResponse);
            resourceDao.addDynamicRequestLog(requestLog);

        }
    }
}
