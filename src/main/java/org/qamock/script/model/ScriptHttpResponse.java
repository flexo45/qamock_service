package org.qamock.script.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ScriptHttpResponse implements Serializable {

    private static final long serialVersionUID = 3465742354633546835L;

    public ScriptHttpResponse(int code){
        this.code = code;
        headers = new HashMap<String, String>();
    }

    public int getCode(){return code;}

    public Map<String, String> getHeaders(){return headers;}

    public String getContent() {return content;}

    public void setHeaders(Map<String, String> v ){headers  = v; }

    public void setContent(String v){content = v; }

    @Override
    public String toString(){
        return "ScriptHttpResponse{code=" + code +
                ", headers=" + headers +
                "content=" + content + "}";
    }


    private int code;

    private Map<String, String> headers;

    private String content;

}
