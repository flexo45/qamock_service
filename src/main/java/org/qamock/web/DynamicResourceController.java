package org.qamock.web;

import org.qamock.api.json.LogRow;
import org.qamock.dynamic.DynamicResourceException;
import org.qamock.dynamic.domain.DynamicResourceRequest;
import org.qamock.service.DynamicResourcesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Controller
public class DynamicResourceController {

    @Autowired
    DynamicResourcesService dynamicResourceService;

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceController.class);

    @PostMapping(value = "/dynamic/resource/**")
    public void dispatchPayload(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestHeader(value = "Content-Type", defaultValue = "text/plain") String type,
                                @RequestBody String content) throws DynamicResourceException{

        logger.info("Receive payload request: url=" + request.getRequestURI() + ", content-type=" + type + ", payload=" + content);

        if(type.equals("application/x-www-form-urlencoded")){
            if(content != null){

                if(request.getQueryString() != null){
                    content = content.replace(request.getQueryString() + "&", "");
                }

                try {
                    content = URLDecoder.decode(content, "UTF-8");
                }
                catch (UnsupportedEncodingException uee){
                    logger.error("x-www-form-urlencoded decoding error", uee);
                }
            }
        }

        DynamicResourceRequest resourceRequest = new DynamicResourceRequest(request, response, content);

        try {
            dynamicResourceService.receiveDynamicResourceRequest(resourceRequest);

            logger.info("Request processed successful");
        }
        catch (Exception e){
            throw new DynamicResourceException("Error occurred on receiving resource request", e);
        }

    }

    @RequestMapping(value = "/dynamic/resource/**")
    public void dispatch(HttpServletRequest request, HttpServletResponse response) throws DynamicResourceException{

        logger.info("Receive request: url=" + request.getRequestURI());

        DynamicResourceRequest resourceRequest = new DynamicResourceRequest(request, response);

        try {
            dynamicResourceService.receiveDynamicResourceRequest(resourceRequest);

            logger.info("Request processed successful");
        }
        catch (Exception e){
            throw new DynamicResourceException("Error occurred on receiving resource request", e);
        }
    }

    @GetMapping(value = "/dynamic/logs", params = "size")
    @ResponseBody
    public List<LogRow> showLogs(@RequestParam("size") int size){
        return dynamicResourceService.getResourceLog(size, null);
    }

    @GetMapping(value = "/dynamic/logs", params = {"size", "resource"})
    @ResponseBody
    public List<LogRow> showLogs(@RequestParam("size") int size, @RequestParam("resource") String resource){
        return dynamicResourceService.getResourceLog(size, resource);
    }

    @GetMapping(value = "/dynamic/logs", params = "resource")
    @ResponseBody
    public List<LogRow> showLogs(@RequestParam("resource") String resource){
        return dynamicResourceService.getResourceLog(-1, resource);
    }

    @GetMapping(value = "/dynamic/logs")
    @ResponseBody
    public List<LogRow> showLogs(){
        return dynamicResourceService.getResourceLog(-1, null);
    }

    @RequestMapping(value = "/default_data")
    private void addTest(HttpServletRequest request, HttpServletResponse response){
        dynamicResourceService.addTest();
    }
}
