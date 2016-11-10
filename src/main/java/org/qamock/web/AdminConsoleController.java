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
    public String index(){
        return "admin";
    }

    @PostMapping(value = "/admin/tools/jms")
    public String jms(){
        return "jms";
    }

    @PostMapping(value = "/admin/dynamic")
    public String dynamicResources(Model model){
        List<DynamicResourceModel> resourceModels = new ArrayList<DynamicResourceModel>();
        for(DynamicResource res : resourcesService.getResourceList()){
            resourceModels.add(new DynamicResourceModel(res, resourcesService.getResponseListOfResource(res.getId())));
        }
        model.addAttribute("dynamic_resources", resourceModels);
        return "resources";
    }

    @PostMapping(value = "admin/dynamic/{id}")
    public String viewResource(@PathVariable("id") long id, Model model){
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
        model.addAttribute("script", resourcesService.getResourceScript(id));
        return "resource_view";
    }

    @PostMapping(value = "admin/dynamic/create")
    public String createResource(Model model){

        model.addAttribute("resource", new ResourceObject());

        return "resource_create";
    }

    @PostMapping(value = "admin/dynamic/{id}/edit")
    public String editResource(@PathVariable("id") long id){
        return "resource_edit";
    }

    @PostMapping(value = "admin/dynamic/{id}/response/{rsp_id}")
    public String viewResponse(@PathVariable("id") long id, @PathVariable("rsp_id") long rsp_id){
        return "response_view";
    }

    @PostMapping(value = "admin/dynamic/{id}/response/add")
    @ResponseBody
    public ApiResult addResponse(@PathVariable("id") long id, @RequestBody ResponseObject responseObject){
        ApiResult result = new ApiResult();
        responseObject.setResource_id(id);
        resourcesService.createResponse(responseObject);
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
