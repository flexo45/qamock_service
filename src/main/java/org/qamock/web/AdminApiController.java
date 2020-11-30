package org.qamock.web;

import org.qamock.api.json.ApiResult;
import org.qamock.api.json.ResourceObject;
import org.qamock.api.json.ResponseObject;
import org.qamock.api.json.UserObject;
import org.qamock.domain.ScriptSuite;
import org.qamock.service.AccountService;
import org.qamock.service.DynamicResourcesService;
import org.qamock.service.ScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminApiController {

    private static final Logger logger = LoggerFactory.getLogger(AdminApiController.class);

    @Autowired
    AccountService accountService;

    @Autowired
    DynamicResourcesService resourcesService;

    @Autowired
    ScriptService scriptService;

    @PostMapping(value = "/admin/scripts/add")
    @ResponseBody
    public ApiResult addScript(@ModelAttribute ScriptSuite scriptSuite){
        scriptService.createSuite(scriptSuite);
        return new ApiResult(0, "OK");
    }

    @RequestMapping(value = "/admin/scripts/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult updateScript(@PathVariable("id") long id, @ModelAttribute ScriptSuite scriptSuite){
        scriptSuite.setId(id);
        scriptService.updateSuite(scriptSuite);
        return new ApiResult(0, "OK");
    }

    @RequestMapping(value = "/admin/dynamic/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult deleteResource(@PathVariable("id") long id){
        ApiResult result = new ApiResult();
        resourcesService.deleteResource(id);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

    @PostMapping(value = "/admin/dynamic/{id}/response/add")
    @ResponseBody
    public ApiResult addResponse(@PathVariable("id") long id, @ModelAttribute ResponseObject responseObject){
        ApiResult result = new ApiResult();
        responseObject.setResource_id(id);
        resourcesService.createResponse(responseObject);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

    @RequestMapping(value = "/admin/dynamic/{id}/response/{rsp_id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult deleteResponse(@PathVariable("id") long id, @PathVariable("rsp_id") long rsp_id){
        ApiResult result = new ApiResult();
        resourcesService.deleteResponse(rsp_id);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

    @RequestMapping(value = "/admin/dynamic/{id}/response/{rsp_id}/update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult updateResponse(@PathVariable("id") long id, @PathVariable("rsp_id") long rsp_id, @ModelAttribute ResponseObject responseObject){
        ApiResult result = new ApiResult();
        responseObject.setId(rsp_id);
        responseObject.setResource_id(id);
        resourcesService.updateResponse(responseObject);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

    @RequestMapping(value = "/admin/dynamic/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult updateResourceMethod(@PathVariable("id") long id, @ModelAttribute ResourceObject resourceObject){
        logger.info("receive update resource POST request. resource_id={}, object={}", id, resourceObject);
        logger.debug("DEBUG ENABLED");
        ApiResult result = new ApiResult();
        resourceObject.setId(id);
        resourcesService.updateResource(resourceObject);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

    @PostMapping(value = "/admin/dynamic/add")
    @ResponseBody
    public ApiResult addResource(@ModelAttribute ResourceObject resourceObject){
        ApiResult result = new ApiResult();
        resourcesService.createResource(resourceObject);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

    @PostMapping(value = "/admin/accounts/add")
    @ResponseBody
    public ApiResult addUser(@ModelAttribute UserObject userObject){
        ApiResult result = new ApiResult();
        accountService.addAccount(userObject);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

}
