package org.qamock.app.main.dynamic;

import org.jetbrains.annotations.NotNull;
import org.qamock.app.main.domain.*;
import org.qamock.app.main.dynamic.context.ContextProcessor;
import org.qamock.app.main.dynamic.datagen.DataGeneratorProcessor;
import org.qamock.app.main.dynamic.domain.DynamicResourceRequest;
import org.qamock.app.main.dynamic.script.ScriptHandler;
import org.qamock.app.main.service.DynamicResourcesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class DynamicResourceRequestHandlerImpl implements DynamicResourceRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceRequestHandlerImpl.class);

    public DynamicResourceRequestHandlerImpl(ScriptHandler scriptHandler,
                                             ContextProcessor contextProcessor,
                                             DataGeneratorProcessor dataGeneratorProcessor) {
        this.scriptHandler = scriptHandler;
        this.contextProcessor = contextProcessor;
        this.dataGeneratorProcessor = dataGeneratorProcessor;
    }

    private final ScriptHandler scriptHandler;
    private final ContextProcessor contextProcessor;
    private final DataGeneratorProcessor dataGeneratorProcessor;
    private DynamicResourcesService resourcesService;

    public void setDynamicResourcesService(DynamicResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    public void processResourceRequest(@NotNull DynamicResource resource,
                                       @NotNull DynamicResourceRequest resourceRequest) throws DynamicResourceException {


        DynamicResponse nextResponse = new DynamicResponse("default", 200, resource);

        List<DynamicResponse> allResponses = resourcesService.getResponseListOfResource(resource.getId());

        Map<String, String> params = new HashMap<String, String>();

        if(allResponses.size() != 0){
            switch (resource.getDispatch_strategy()){
                case 0:
                    //SEQUENCE
                    DynamicResponse lastResponse = resource.getLastDynamicResponse();
                    long lastResponseId;
                    if(lastResponse == null){
                        lastResponseId = allResponses.get(0).getId();
                    }
                    else {
                        lastResponseId = lastResponse.getId();
                    }
                    for(DynamicResponse response : allResponses){
                        if(response.getId() == lastResponseId){
                            int cur_idx = allResponses.indexOf(response);
                            if(cur_idx + 1 < allResponses.size()){
                                nextResponse = allResponses.get(cur_idx + 1);
                            }
                            else {
                                nextResponse = allResponses.get(0);
                            }
                        }
                    }
                    break;
                case 1:
                    //RANDOM
                    int rnd_idx = new Random().nextInt(allResponses.size());
                    nextResponse = allResponses.get(rnd_idx == 0 ? 0 : rnd_idx - 1);
                    break;
                case 2:
                    //SCRIPT
                    Script script = resourcesService.getResourceScript(resource.getId());
                    if(script != null){
                        try {
                            Map<String, Object> resultMap = scriptHandler.executeDispatchScript(script.getText(), params, resourceRequest);
                            String responseName = (String) resultMap.get("response");
                            if(responseName != null){

                                for(DynamicResponse response : allResponses){
                                    if(response.getName().equals(responseName)){
                                        nextResponse = response;
                                        break;
                                    }
                                }

                                if(nextResponse.getId() <= 0) {
                                    logger.warn("Response with name: " + responseName + " not found for: " + resource);
                                }
                                else {
                                    logger.info("Found response from script: " + nextResponse);
                                }
                            }
                            else {
                                logger.warn("Script didn't return any response name, for resource: " + resource + ", user default response{status=200}");
                            }
                        }
                        catch (RuntimeException re){
                            logger.error("Dispatch script has error, resource: " + resource, re);
                            nextResponse.setCode(500);
                            nextResponse.setName("script_error");
                        }
                    }
                    else {
                        logger.warn("Script not found for SCRIPT dispatched resource: " + resource);
                    }
                    break;
            }

            if(nextResponse.getId() > 0){
                resource.setLastDynamicResponse(nextResponse);
                resourcesService.updateResource(resource);
            }

            if(params.size() > 0){
                processResponse(nextResponse, resourceRequest, params);
            }
            else {
                processResponse(nextResponse, resourceRequest);
            }

        }
        else {
            logger.warn("No responses found for resource: " + resource + ", user default response{status=200}");
            processResponse(nextResponse, resourceRequest);
        }
    }

    private void processResponse(DynamicResponse dynamicResponse,
                                 DynamicResourceRequest resourceRequest,
                                 Map<String, String> params) throws DynamicResourceException{

        logger.info("Response processing started: " + dynamicResponse + ", with params: " + params);

        HttpServletResponse response = resourceRequest.response();
        response.setStatus(dynamicResponse.getCode());

        for(Header header : resourcesService.getHeadersOfResponse(dynamicResponse.getId())){
            response.addHeader(header.getName(), resolveParamsExpressions(header.getValue(), params));
        }

        Content content = resourcesService.getContentOfResponse(dynamicResponse.getId());

        processResponseScript(dynamicResponse, resourceRequest, params);

        if(content != null){
            try {
                String contentString = resolveParamsExpressions(content.getText(), params);
                resourceRequest.setResponseContent(contentString);
                response.getWriter().print(contentString);
            }
            catch (IOException e){
                throw new DynamicResourceException("Error occurred on write response content", e);
            }
        }

        logger.info("Complete process response: " + response);

    }

    private void processResponse(DynamicResponse dynamicResponse,
                                 DynamicResourceRequest resourceRequest) throws DynamicResourceException{

        logger.info("Response processing started: " + dynamicResponse);

        Content content;

        HttpServletResponse response = resourceRequest.response();
        response.setStatus(dynamicResponse.getCode());

        for(Header header : resourcesService.getHeadersOfResponse(dynamicResponse.getId())){
            response.addHeader(header.getName(), header.getValue());
        }

        if(dynamicResponse.getName().equals("default")){
            response.addHeader("Content-Type", "application/json");
            content = new Content("{\"code\":200,\"text\":\"default mock response\"}", dynamicResponse);
        }
        else if(dynamicResponse.getName().equals("script_error")){
            response.addHeader("Content-Type", "application/json");
            content = new Content("{\"code\":500,\"text\":\"dispatch script error\"}", dynamicResponse);
        }
        else {
            content = resourcesService.getContentOfResponse(dynamicResponse.getId());
        }

        processResponseScript(dynamicResponse, resourceRequest);

        if(content != null){
            try {
                String contentString = content.getText();
                resourceRequest.setResponseContent(contentString);
                response.getWriter().print(contentString);
            }
            catch (IOException e){
                throw new DynamicResourceException("Error occurred on write response content", e);
            }
        }

        logger.info("Complete process response: " + response);

    }

    private void processResponseScript(DynamicResponse dynamicResponse,
                                       DynamicResourceRequest resourceRequest){
        logger.info("Response Script processing started");

        Script script = resourcesService.getResponseScript(dynamicResponse.getId());
        if(script != null){
            logger.info("Found script: " + script);
            scriptHandler.executeResponseScript(script.getText(), resourceRequest.response());
        }
    }

    private void processResponseScript(DynamicResponse dynamicResponse,
                                       DynamicResourceRequest resourceRequest,
                                       Map<String, String> params){
        logger.info("Response Script processing started, with params: " + params);

        Script script = resourcesService.getResponseScript(dynamicResponse.getId());
        if(script != null){
            logger.info("Found script: " + script);
            scriptHandler.executeResponseScript(script.getText(), params, resourceRequest.response());
        }
    }

    private String resolveParamsExpressions(String target, Map<String, String> params) {
        return replaceDataGeneration(replaceParams(replaceContextParams(target, params), params));
    }

    private String replaceParams(String target, Map<String, String> params){
        String result = target;
        for(Map.Entry<String, String> param : params.entrySet()){
            try {
                result = result.replace("${#params#" + param.getKey() +"}", param.getValue());
            } catch (Exception e) {
                logger.warn("Somting wrong with replace " + param.getKey());
            }
        }
        return result;
    }

    private String replaceContextParams(String target, Map<String, String> params){
        return contextProcessor.process(target, params);
    }

    private String replaceDataGeneration(String target) {
        return dataGeneratorProcessor.process(target);
    }

}
