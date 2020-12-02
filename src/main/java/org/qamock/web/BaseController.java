package org.qamock.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Controller
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @RequestMapping(value = "/login")
    public String loginView(){
        return "login";
    }

    @RequestMapping(value = "/forbidden")
    public String forbiddenView(){
        return "error403";
    }

    /*@RequestMapping(value = "/error")
    public String errorView(){
        return "error500";
    }*/

    @GetMapping(value = "/") //TODO change to /
    public void toIndexView(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Location", request.getRequestURL().toString() + "admin");
        response.setStatus(HttpServletResponse.SC_FOUND);
    }

    /*TEST DEBUG*/
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response){
        File file = new File("TEST.CALLBACK");
        try {
            file.createNewFile();
        }
        catch (IOException e){}

        toIndexView(request, response);
    }

}
