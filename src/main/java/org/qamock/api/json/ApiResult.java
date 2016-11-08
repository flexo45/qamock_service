package org.qamock.api.json;

import java.io.Serializable;

public class ApiResult implements Serializable{

    private static final long serialVersionUID = 2754657423657468548L;

    private int statusCode;
    private String statusText;

    public int getStatusCode(){return statusCode;}
    public void setStatusCode(int statusCode){this.statusCode = statusCode;}

    public String getStatusText(){return statusText;}
    public void setStatusText(String statusText){this.statusText = statusText;}

}
