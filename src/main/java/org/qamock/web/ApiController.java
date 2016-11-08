package org.qamock.web;

import com.temafon.data.MORequest;
import com.temafon.data.MTRequest;
import org.qamock.api.json.ApiResult;
import org.qamock.api.json.MOMessage;
import org.qamock.api.json.MTMessage;
import org.qamock.api.json.TextMessage;
import org.qamock.service.JmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ApiController {

    @Autowired
    private JmsService jmsService;

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

    @RequestMapping(value = "/api/jms/mt", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult mtJmsHandler(@RequestBody MTMessage mtMessage){

        ApiResult result = new ApiResult();

        try {
            if(mtMessage != null){

                Map<String, String> jmsProp = new HashMap<String, String>();

                if(mtMessage.getProperties() != null){
                    for(String s : mtMessage.getProperties()){
                        String[] p_arr = s.split(":");
                        if(p_arr.length > 1){
                            jmsProp.put(p_arr[0], p_arr[1]);
                        }
                        else {
                            //TODO log fail parsing
                        }
                    }
                }

                MTRequest mtRequest = new MTRequest(mtMessage.getMsisdn(), mtMessage.getShort_number(), mtMessage.getMessage_text(), mtMessage.getService_name());


                jmsService.sendObjectMessage(mtMessage.getServer(), mtMessage.getQueue(), mtRequest, jmsProp);

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

    @RequestMapping(value = "/api/jms/mo", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult moJmsHandler(@RequestBody MOMessage moMessage){

        ApiResult result = new ApiResult();

        try {
            if(moMessage != null){

                Map<String, String> jmsProp = new HashMap<String, String>();

                if(moMessage.getProperties() != null){
                    for(String s : moMessage.getProperties()){
                        String[] p_arr = s.split(":");
                        if(p_arr.length > 1){
                            jmsProp.put(p_arr[0], p_arr[1]);
                        }
                        else {
                            //TODO log fail parsing
                        }
                    }
                }

                MORequest moRequest = new MORequest(moMessage.getMsisdn(), moMessage.getOrdering_channel(), moMessage.getMessage_text(), moMessage.getService_id());

                jmsService.sendObjectMessage(moMessage.getServer(), moMessage.getQueue(), moRequest, jmsProp);

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
