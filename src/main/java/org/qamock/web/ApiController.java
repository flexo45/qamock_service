package org.qamock.web;

import org.hibernate.annotations.Parameter;
import org.qamock.api.json.*;
import org.qamock.service.EmailService;
import org.qamock.service.JmsService;
import org.qamock.xml.XmlProcessor;
import org.qamock.xml.object.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ApiController {

    private static Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    JmsService jmsService;

    @Autowired
    EmailService emailService;

    @Autowired
    XmlProcessor xmlProcessor;

    @PostMapping(value = "/api/jms/object")
    public ApiResult sendJmsObject(@ModelAttribute JmsMessage jmsMessage){
        return new ApiResult();
    }

    @PostMapping(value = "/api/mail")
    @ResponseBody
    public ApiResult sendMail(@RequestBody String content){
        ApiResult result = new ApiResult();



        try {
            Email email = (Email) xmlProcessor.stringToObject(content);
            if(email.getTo() == null || email.getMessage() == null || email.getSubject() == null){
                result.setStatusCode(400);
                result.setStatusText("Required parameters is null");
            }
            else {
                emailService.sendMail(email);
                result.setStatusText("success");
                result.setDescription("email was sanded");
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            result.setStatusCode(500);
            result.setStatusText("Error on parsing xml");
            result.setDescription(ioe.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/api/jms/text", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult textJmsHandler(@RequestBody TextMessage textMessage){

        ApiResult result = new ApiResult();

        try {
            if(textMessage != null){

                Map<String, String> jmsProp = new HashMap<String, String>();

                if(textMessage.getProperties() != null){
                    for(String s : textMessage.getProperties()){
                        String[] p_arr = s.split(":");
                        if(p_arr.length > 1){
                            jmsProp.put(p_arr[0], p_arr[1]);
                        }
                        else {
                            //TODO log fail parsing
                        }
                    }
                }

                jmsService.sendTextMessage(textMessage.getServer(), textMessage.getQueue(), textMessage.getText(), jmsProp);

                result.setStatusCode(0);
                result.setStatusText("OK");
            }
        }
        catch (JMSException jms_e){
            result.setStatusCode(300);
            result.setStatusText("JMS Exception - " + jms_e.getMessage());
        }
        catch (Exception e){
            result.setStatusCode(500);
            result.setStatusText("Unexpected Exception - " + e.toString() + " - " + e.getMessage());
        }

        return result;
    }
}
