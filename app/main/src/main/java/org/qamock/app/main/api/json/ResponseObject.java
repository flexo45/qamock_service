package org.qamock.app.main.api.json;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class ResponseObject implements Serializable {

    private static final long serialVersionUID = 4765235423543565658L;

    private long id;
    private String name;
    private String content;
    private int code;
    private long resource_id;
    private List<String> headers;
    private String script;

    public long getId(){return id;}
    public void setId(long id){this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getContent(){return content;}
    public void setContent(String content){
        try {
            this.content = URLDecoder.decode(content, "UTF-8");
        }
        catch (UnsupportedEncodingException e){
            this.content = e.getMessage();
        }
    }

    public int getCode(){return code;}
    public void setCode(int code){this.code = code;}

    public long getResource_id(){return resource_id;}
    public void setResource_id(long resource_id){this.resource_id = resource_id;}

    public List<String> getHeaders(){return headers;}
    public void setHeaders(List<String> headers){this.headers = headers;}

    public String getScript(){return script;}
    public void setScript(String script){this.script = script;}

    @Override
    public String toString(){
        return "ResponseObject{name=" + name +
                ", content=" + content +
                ", code=" + code +
                ", resource_id=" + resource_id +
                ", headers=" + headers +
                ", script=" + script + "}";
    }
}
