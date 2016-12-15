package org.qamock.api.json;

import java.io.Serializable;

public class ApiResult implements Serializable{

    private static final long serialVersionUID = 2754657423657468548L;

    public ApiResult(){}

    public ApiResult(int code, String text){
        setStatusCode(code);
        setStatusText(text);
    }

    private int statusCode;
    private String statusText;
    private String description;

    public int getStatusCode(){return statusCode;}
    public void setStatusCode(int statusCode){this.statusCode = statusCode;}

    public String getStatusText(){return statusText;}
    public void setStatusText(String statusText){this.statusText = statusText;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

}
