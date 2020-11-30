package org.qamock.api.json;

import org.qamock.domain.MockRequest;
import org.qamock.domain.MockResponse;

import java.io.Serializable;

public class LogRow implements Serializable {

    private static final long serialVersionUID = 4253742365424566558L;

    private long id;
    private String resource;
    private String timestamp;
    private MockRequestPojo request;
    private MockResponse response;

    public long getId(){return this.id;}
    public void setId(long id){this.id = id;}

    public String getResource(){return this.resource;}
    public void setResource(String resource){this.resource = resource;}

    public String getTimestamp(){return this.timestamp;}
    public void setTimestamp(String timestamp){this.timestamp = timestamp;}

    public MockRequestPojo getRequest(){return this.request;}
    public void setRequest(MockRequestPojo request){this.request = request;}

    public MockResponse getResponse(){return  this.response;}
    public void setResponse(MockResponse response){this.response = response;}

}
