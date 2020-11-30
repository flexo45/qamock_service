package org.qamock.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GraphController {

    private static final Logger logger = LoggerFactory.getLogger(GraphController.class);

    @RequestMapping(value = "/graph")
    public String graphView(Model model, HttpServletRequest request) throws Exception{

        String data_pattern = "function getData(){return %;}";

        String query = request.getQueryString();

        //if(query.contains())

        Map<String, String> options = new HashMap<String, String>();

        options.put("time", null);
        options.put("rps", null);
        options.put("vu", null);

        BufferedReader reader = new BufferedReader(new FileReader("graph/result.csv"));

        StringBuilder jsDataSB = new StringBuilder();

        jsDataSB.append("[[");
        for(Map.Entry<String, String> it : options.entrySet()){
            jsDataSB.append("'" + it.getKey() + "',");
        }
        jsDataSB.deleteCharAt(jsDataSB.length() - 1);
        jsDataSB.append("],");


        String line;
        while ((line = reader.readLine()) != null){
            String[] data = line.split(";");
            if(options.get("time") != null){options.put("time", data[0]);}
            if(options.get("rps") != null){options.put("rps", data[1]);}
            if(options.get("vu") != null){options.put("vu", data[data.length - 1]);}
            jsDataSB.append("[");
            for(Map.Entry<String, String> it : options.entrySet()){
                jsDataSB.append(it.getValue() + ",");
            }
            jsDataSB.deleteCharAt(jsDataSB.length() - 1);
            jsDataSB.append("],");
        }
        reader.close();

        jsDataSB.deleteCharAt(jsDataSB.length() - 1);
        jsDataSB.append("]");

        String data_file = "static/graph/data_hash.js";
        BufferedWriter writer = new BufferedWriter(new FileWriter(data_file));

        writer.write(data_pattern.replace("%", jsDataSB.toString()));
        writer.flush();
        writer.close();

        model.addAttribute("data_file", data_file);

        return "graph";
    }

}
