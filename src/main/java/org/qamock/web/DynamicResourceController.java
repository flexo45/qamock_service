package org.qamock.web;

import org.qamock.dao.DynamicResourceDao;
import org.qamock.domain.DynamicResource;
import org.qamock.dynamic.DynamicResourceException;
import org.qamock.dynamic.domain.DynamicResourceRequest;
import org.qamock.service.DynamicResourceServiceImpl;
import org.qamock.service.DynamicResourcesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class DynamicResourceController {

    @Autowired
    DynamicResourcesService dynamicResourceService;

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceController.class);

    @RequestMapping(value = "/dynamic/**")
    public void dispatch(HttpServletRequest request, HttpServletResponse response) throws Exception{

        logger.info("Receive request - " + request.getRequestURI());

        DynamicResourceRequest resourceRequest = new DynamicResourceRequest(request, response);

        try {
            dynamicResourceService.receiveDynamicResourceRequest(resourceRequest);
        }
        catch (DynamicResourceException e){
            throw e;
        }
    }

    @RequestMapping(value = "/default_data")
    private void addTest(HttpServletRequest request, HttpServletResponse response){
        dynamicResourceService.addTest();
    }
}
