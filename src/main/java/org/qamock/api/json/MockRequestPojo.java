package org.qamock.api.json;

import java.util.Map;

public class MockRequestPojo {

    private long id;
    public long getId(){return id;}
    public void setId(long id) {this.id = id;}

    private String method;
    public String getMethod() {return method;}
    public void setMethod(String method) {this.method = method;}

    private String path;
    public String getPath() {return path;}
    public void setPath(String path) {this.path = path;}

    private Map<String, String> headers;
    public Map<String, String> getHeaders() {return headers;}
    public void setHeaders(Map<String, String> headers) {this.headers = headers;}

    private String content;
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

}
