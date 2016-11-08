package org.qamock.api.json;

import java.io.Serializable;
import java.util.List;

public class MOMessage implements Serializable {

    private static final long serialVersionUID = 7665345643542398450L;

    private long msisdn;
    private long service_id;
    private String ordering_channel;
    private String message_text;
    private String short_number;
    private int source_port;
    private String queue;
    private String server;
    private List<String> properties;

    public long getMsisdn(){return msisdn;}
    public void setMsisdn(long msisdn){this.msisdn = msisdn;}

    public long getService_id(){return service_id;}
    public void setService_id(long service_id){this.service_id = service_id;}

    public String getOrdering_channel(){return ordering_channel;}
    public void setOrdering_channel(String ordering_channel){this.ordering_channel = ordering_channel;}

    public String getMessage_text(){return message_text;}
    public void setMessage_text(String message_text){this.message_text = message_text;}

    public String getShort_number(){return short_number;}
    public void setShort_number(String short_number){this.short_number = short_number;}

    public int getSource_port(){return source_port;}
    public void setSource_port(int source_port){this.source_port = source_port;}

    public String getQueue(){return queue;}
    public void setQueue(String queue){this.queue = queue;}

    public String getServer(){return server;}
    public void setServer(String server){this.server = server;}

    public List<String> getProperties(){return properties;}
    public void setProperties(List<String> properties){this.properties = properties;}

}
