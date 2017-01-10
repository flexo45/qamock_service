package org.qamock.api.json;

import java.io.Serializable;
import java.util.List;

public class MTRespMessage implements Serializable {

    private static final long serialVersionUID = 4357694764545784975L;

    private long msisdn;
    private String service_name;
    private String message_text;
    private String short_number;
    private String guid;
    private long status;
    private String err_text;

    private String queue;
    private String server;
    private List<String> properties;

    public long getMsisdn(){return msisdn;}
    public void setMsisdn(long msisdn){this.msisdn = msisdn;}

    public String getService_name(){return service_name;}
    public void setService_name(String service_name){this.service_name = service_name;}

    public String getMessage_text(){return message_text;}
    public void setMessage_text(String message_text){this.message_text = message_text;}

    public String getShort_number(){return short_number;}
    public void setShort_number(String short_number){this.short_number = short_number;}

    public String getGuid(){return guid;}
    public void setGuid(String guid){this.guid = guid;}

    public long getStatus(){return status;}
    public void setStatus(long status){this.status = status;}

    public String getErr_text(){return err_text;}
    public void setErr_text(String err_text){this.err_text = err_text;}


    public String getQueue(){return queue;}
    public void setQueue(String queue){this.queue = queue;}

    public String getServer(){return server;}
    public void setServer(String server){this.server = server;}

    public List<String> getProperties(){return properties;}
    public void setProperties(List<String> properties){this.properties = properties;}

}
