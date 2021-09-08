package org.qamock.app.main.dynamic.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.GsonJsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicResourceRequest implements Serializable {

    private static final long serialVersionUID = 5235267583576843013L;

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceRequest.class);

    public DynamicResourceRequest(HttpServletRequest request, HttpServletResponse response, String content){

        this.response = response;

        this.method = request.getMethod();

        this.path = request.getRequestURI();

        this.query = request.getQueryString();

        this.headers = getHeadersMap(request);

        this.content = content;
    }

    public DynamicResourceRequest(HttpServletRequest request, HttpServletResponse response){

        this.response = response;

        this.method = request.getMethod();

        this.path = request.getRequestURI();

        this.query = request.getQueryString();

        this.headers = getHeadersMap(request);

        this.content = null;
    }

    private String  method;
    private String path;
    private String query;
    private Map<String, String> headers;
    private String content;
    private HttpServletResponse response;
    private String responseContent;

    public String method() {return this.method;}

    public String path() {return this.path;}

    public String query() {return this.query;}

    public Map<String, String> headers() {return this.headers;}

    public String content() {return this.content;}

    public Map<String, Object> jobject() { return new GsonJsonParser().parseMap(this.content); }

    public List<Object> jarray() { return new GsonJsonParser().parseList(this.content); }

    public HttpServletResponse response() {return this.response;}

    public String getResponseContent() {return this.responseContent;}

    public void setResponseContent(String responseContent) {this.responseContent = responseContent;}




    public static Map<String, String> getHeadersMap(HttpServletRequest request){

        Map<String, String> result = new HashMap<String, String>();

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()){
            String header = headerNames.nextElement();
            result.put(header, request.getHeader(header));
        }
        return result;
    }

    @Deprecated
    public static String getContent(InputStream inputStream) throws IOException{

        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;

        //logger.info("stream state=" + inputStream.available());

        //logger.info("reader state: ready=" + bufferedReader.ready());

        while ((line = bufferedReader.readLine()) != null){
            //logger.info("read content line=" + line);
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
