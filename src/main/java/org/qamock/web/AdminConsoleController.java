package org.qamock.web;

import org.qamock.domain.DynamicResource;
import org.qamock.domain.DynamicResourceMethod;
import org.qamock.domain.DynamicResponse;
import org.qamock.domain.User;
import org.qamock.service.AccountService;
import org.qamock.service.DynamicResourcesService;
import org.qamock.service.JmsService;
import org.qamock.web.mvc.DynamicResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AdminConsoleController {

    private static final Logger logger = LoggerFactory.getLogger(AdminConsoleController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private DynamicResourcesService resourcesService;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String index(){
        return "admin";
    }

    @RequestMapping(value = "/admin/tools/jms", method = RequestMethod.POST)
    public String jms(){
        return "jms";
    }

    @RequestMapping(value = "/admin/dynamic", method = RequestMethod.POST)
    public String dynamicResources(Model model){
        List<DynamicResourceModel> resourceModels = new ArrayList<DynamicResourceModel>();
        for(DynamicResource res : resourcesService.getResourceList()){
            resourceModels.add(new DynamicResourceModel(res, resourcesService.getResponseListOfResource(res.getId())));
        }
        model.addAttribute("dynamic_resources", resourceModels);
        return "resources";
    }

    @RequestMapping(value = "admin/dynamic/{id}", method = RequestMethod.POST)
    public String resourceById(@PathVariable("id") long id, Model model){
        logger.info("Incoming id=" + id);
        DynamicResource resource = resourcesService.getResource(id);
        logger.info("Loading resource: " + resource);
        model.addAttribute("id", resource.getId());
        model.addAttribute("path", resource.getPath());
        model.addAttribute("strategy", resource.getDispatch_strategy());
        model.addAttribute("default_resp", resource.getDefaultDynamicResponse());
        model.addAttribute("last_resp", resource.getLastDynamicResponse());
        model.addAttribute("responses", resourcesService.getResponseListOfResource(id));
        model.addAttribute("methods", resourcesService.getAcceptanceMethods(id));
        return "resource_view";
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
