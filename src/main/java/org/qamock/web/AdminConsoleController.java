package org.qamock.web;

import org.dom4j.rule.Mode;
import org.qamock.api.json.ApiResult;
import org.qamock.api.json.ResourceObject;
import org.qamock.api.json.ResponseObject;
import org.qamock.domain.*;
import org.qamock.service.AccountService;
import org.qamock.service.DynamicResourcesService;
import org.qamock.web.mvc.DynamicResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminConsoleController {

    private static final Logger logger = LoggerFactory.getLogger(AdminConsoleController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private DynamicResourcesService resourcesService;

    @GetMapping(value = "/admin")
    public String indexView(){
        return "admin";
    }

    @GetMapping(value = "/admin/tools/jms")
    public String jmsView(){
        return "jms";
    }

    @GetMapping(value = "/admin/dynamic")
    public String dynamicResourcesView(Model model){
        List<DynamicResourceModel> resourceModels = new ArrayList<DynamicResourceModel>();
        for(DynamicResource res : resourcesService.getResourceList()){
            resourceModels.add(new DynamicResourceModel(res, resourcesService.getResponseListOfResource(res.getId())));
        }
        model.addAttribute("dynamic_resources", resourceModels);
        return "resources";
    }

    @GetMapping(value = "admin/dynamic/{id}")
    public String resourceView(@PathVariable("id") long id, Model model){
        //logger.debug("Incoming id=" + id);
        DynamicResource resource = resourcesService.getResource(id);
        //logger.debug("Loading resource: " + resource);
        model.addAttribute("id", resource.getId());
        model.addAttribute("path", resource.getPath());
        model.addAttribute("strategy", resource.getDispatch_strategy());
        model.addAttribute("default_resp", resource.getDefaultDynamicResponse());
        model.addAttribute("last_resp", resource.getLastDynamicResponse());
        model.addAttribute("responses", resourcesService.getResponseListOfResource(id));
        model.addAttribute("methods", resourcesService.getAcceptanceMethods(id));

        Script script = resourcesService.getResourceScript(id);
        if(script != null){
            model.addAttribute("script", script.getText());
        }

        return "resource_view";
    }

    @GetMapping(value = "admin/dynamic/create")
    public String createResourceView(Model model){
        model.addAttribute("resource", new ResourceObject());
        return "resource_create";
    }

    @GetMapping(value = "admin/dynamic/{id}/edit")
    public String editResourceView(@PathVariable("id") long id, Model model){
        DynamicResource resource = resourcesService.getResource(id);
        ResourceObject object = new ResourceObject();
        object.setId(id);
        object.setPath(resource.getPath());

        List<String> methods = new ArrayList<String>();
        for(DynamicResourceMethod method : resourcesService.getAcceptanceMethods(id)){
            methods.add(method.getMethod());
        }
        object.setMethods(methods);

        object.setStrategy(resource.getDispatch_strategy());

        object.setDefault_resp(resource.getDefaultDynamicResponse() == null ? 0 : resource.getDefaultDynamicResponse().getId());

        Script script = resourcesService.getResourceScript(id);
        object.setScript(script == null ? null : script.getText());

        model.addAttribute("resource", object);
        //model.addAttribute("default_response", resource.getDefaultDynamicResponse());
        model.addAttribute("responses", resourcesService.getResponseListOfResource(id));

        return "resource_edit";
    }

    @GetMapping(value = "admin/dynamic/{id}/response/create")
    public String createResponseView(@PathVariable("id") long id, Model model){
        ResponseObject responseObject = new ResponseObject();
        responseObject.setResource_id(id);
        model.addAttribute("response", responseObject);
        return "response_create";
    }

    @GetMapping(value = "admin/dynamic/{id}/response/{rsp_id}")
    public String responseView(@PathVariable("id") long id, @PathVariable("rsp_id") long rsp_id, Model model){
        DynamicResponse dynamicResponse = resourcesService.getResponse(rsp_id);
        model.addAttribute("id", dynamicResponse.getId());
        model.addAttribute("resource_id", id);
        model.addAttribute("name", dynamicResponse.getName());
        model.addAttribute("code", dynamicResponse.getCode());

        Content content = resourcesService.getContentOfResponse(rsp_id);
        if(content != null){
            model.addAttribute("content", content.getText());
        }

        Script script= resourcesService.getResponseScript(rsp_id);
        if(script != null){
            model.addAttribute("script", script.getText());
        }

        model.addAttribute("headers", resourcesService.getHeadersOfResponse(rsp_id));

        return "response_view";
    }

    @GetMapping(value = "admin/dynamic/{id}/response/{rsp_id}/edit")
    public String editResponseView(@PathVariable("id") long id, @PathVariable("rsp_id") long rsp_id, Model model){
        DynamicResponse dynamicResponse = resourcesService.getResponse(rsp_id);
        ResponseObject object = new ResponseObject();
        object.setId(rsp_id);
        object.setResource_id(id);
        object.setCode(dynamicResponse.getCode());
        object.setName(dynamicResponse.getName());

        Content content = resourcesService.getContentOfResponse(rsp_id);
        if(content != null){
            object.setContent(content.getText());
        }

        Script script = resourcesService.getResponseScript(rsp_id);
        if(script != null){
            object.setScript(script.getText());
        }

        List<String> headers = new ArrayList<String>();
        for(Header hdr : resourcesService.getHeadersOfResponse(rsp_id)){
            headers.add(hdr.getName() + ":" + hdr.getValue());
        }
        object.setHeaders(headers);

        model.addAttribute("response", object);
        return "response_edit";
    }

    @PostMapping(value = "admin/dynamic/{id}/response/add")
    @ResponseBody
    public ApiResult addResponse(@PathVariable("id") long id, @ModelAttribute ResponseObject responseObject){
        ApiResult result = new ApiResult();
        responseObject.setResource_id(id);
        resourcesService.createResponse(responseObject);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

    @PostMapping(value = "admin/dynamic/{id}/response/{rsp_id}/update")
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

    @PostMapping(value = "admin/dynamic/{id}/update")
    @ResponseBody
    public ApiResult updateResource(@PathVariable("id") long id, @ModelAttribute ResourceObject resourceObject){
        ApiResult result = new ApiResult();
        resourceObject.setId(id);
        resourcesService.updateResource(resourceObject);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

    @PostMapping(value = "admin/dynamic/add")
    @ResponseBody
    public ApiResult addResource(@ModelAttribute ResourceObject resourceObject){
        ApiResult result = new ApiResult();
        resourcesService.createResource(resourceObject);
        result.setStatusCode(0);
        result.setStatusText("OK");
        return result;
    }

    @RequestMapping(value = "/admin/accounts", method = RequestMethod.GET)
    public String accountsList(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("userList", accountService.listAccount());

        return "account";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        File file = new File("TEST.CALLBACK");
        try {
            file.createNewFile();
        }
        catch (IOException e){}


        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/accounts/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user, BindingResult result){
        accountService.addAccount(user);

        return "redirect:/admin/accounts";
    }

}
