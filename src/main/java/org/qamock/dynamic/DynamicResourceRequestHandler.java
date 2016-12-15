package org.qamock.dynamic;

import org.qamock.domain.*;
import org.qamock.dynamic.domain.DynamicResourceRequest;
import org.qamock.dynamic.script.GroovyScriptHandler;
import org.qamock.dynamic.script.ScriptHandler;
import org.qamock.service.DynamicResourcesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DynamicResourceRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceRequestHandler.class);

    public DynamicResourceRequestHandler(@NotNull DynamicResourceRequest resourceRequest,
                                         @NotNull DynamicResourcesService resourcesService,
                                         @NotNull ScriptHandler scriptHandler){
        this.scriptHandler = scriptHandler;
        this.resourceRequest = resourceRequest;
        this.resourcesService = resourcesService;
    }

    private ScriptHandler scriptHandler;

    private DynamicResourcesService resourcesService;

    private DynamicResourceRequest resourceRequest;

    public void processResourceRequest(DynamicResource resource) throws DynamicResourceException{

        DynamicResponse nextResponse = new DynamicResponse("default", 200, resource);

        List<DynamicResponse> allResponses = resourcesService.getResponseListOfResource(resource.getId());

        //logger.debug("allResponses=" + allResponses);

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
                    int rnd_idx = new Random().nextInt(allResponses.size() - 1);
                    nextResponse = allResponses.get(rnd_idx);
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
                processResponse(nextResponse, params);
            }
            else {
                processResponse(nextResponse);
            }

        }
        else {
            logger.warn("No responses found for resource: " + resource + ", user default response{status=200}");
            processResponse(nextResponse);
        }
    }

    private void processResponse(DynamicResponse dynamicResponse, Map<String, String> params) throws DynamicResourceException{

        logger.info("Response processing started: " + dynamicResponse + ", with params: " + params);

        HttpServletResponse response = resourceRequest.response();
        response.setStatus(dynamicResponse.getCode());

        for(Header header : resourcesService.getHeadersOfResponse(dynamicResponse.getId())){
            response.addHeader(header.getName(), replaceParams(header.getValue(), params));
        }

        Content content = resourcesService.getContentOfResponse(dynamicResponse.getId());

        if(content != null){
            try {
                String contentString = replaceParams(content.getText(), params);
                resourceRequest.setResponseContent(contentString);
                response.getWriter().print(contentString);
            }
            catch (IOException e){
                throw new DynamicResourceException("Error occurred on write response content", e);
            }
        }

        processResponseScript(dynamicResponse, params);

        logger.info("Complete process response: " + response);

    }

    private void processResponse(DynamicResponse dynamicResponse) throws DynamicResourceException{

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

        processResponseScript(dynamicResponse);

        logger.info("Complete process response: " + response);

    }

    private void processResponseScript(DynamicResponse dynamicResponse){
        logger.info("Response Script processing started");

        Script script = resourcesService.getResponseScript(dynamicResponse.getId());
        if(script != null){
            logger.info("Found script: " + script);
            scriptHandler.executeResponseScript(script.getText(), resourceRequest.response());
        }
    }

    private void processResponseScript(DynamicResponse dynamicResponse, Map<String, String> params){
        logger.info("Response Script processing started, with params: " + params);

        Script script = resourcesService.getResponseScript(dynamicResponse.getId());
        if(script != null){
            logger.info("Found script: " + script);
            scriptHandler.executeResponseScript(script.getText(), params, resourceRequest.response());
        }
    }

    private String replaceParams(String target, Map<String, String> params){
        String result = target;
        for(Map.Entry<String, String> param : params.entrySet()){
            result = result.replace("${#params#" + param.getKey() +"}", param.getValue());
        }
        return result;
    }

}
