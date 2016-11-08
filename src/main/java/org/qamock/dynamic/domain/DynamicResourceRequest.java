package org.qamock.dynamic.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DynamicResourceRequest implements Serializable {

    private static final long serialVersionUID = 5235267583576843013L;

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceRequest.class);

    public DynamicResourceRequest(@NotNull HttpServletRequest request,@NotNull HttpServletResponse response){
        this.response = response;

        this.method = request.getMethod();

        this.path = request.getRequestURI();

        this.query = request.getQueryString();

        this.headers = getHeadersMap(request);

        if(this.method.equals("POST")){
            try {
                ServletInputStream inputStream = request.getInputStream();
                if(inputStream != null) {
                    this.content = getContent(inputStream);
                }
            }
            catch (IOException ioe){
                logger.warn("POST Content not found!", ioe);
            }
        }
        else {
            this.content = null;
        }
    }

    private String  method;
    private String path;
    private String query;
    private Map<String, String> headers;
    private String content;
    private HttpServletResponse response;

    public String method(){return this.method;}

    public String path(){return this.path;}

    public String query(){return this.query;}

    public Map<String, String> headers(){return this.headers;}

    public String content(){return this.content;}

    public HttpServletResponse response(){return this.response;}




    public static Map<String, String> getHeadersMap(@NotNull HttpServletRequest request){

        Map<String, String> result = new HashMap<String, String>();

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()){
            String header = headerNames.nextElement();
            result.put(header, request.getHeader(header));
        }
        return result;
    }

    public static String getContent(@NotNull ServletInputStream inputStream) throws IOException{

        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;

        while ((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }

        bufferedReader.close();

        return stringBuilder.toString();
    }

    @Override
    public String toString(){
        return "DynamicResourceRequest{method=" + method +
                ", path=" + path +
                ", query=" + query +
                ", headers=" + headers +
                ", content=" + content + "}";
    }
}
