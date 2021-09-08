package org.qamock.app.main.api.json;

import java.io.Serializable;
import java.util.List;

public class JmsMessage implements Serializable {

    private static final long serialVersionUID = 1513207428686438208L;

    private String object;
    private String queue;
    private String server;
    private List<String> properties;

    public String getObject(){return object;}
    public void setObject(String object){this.object = object;}

    public String getQueue(){return queue;}
    public void setQueue(String queue){this.queue = queue;}

    public String getServer(){return server;}
    public void setServer(String server){this.server = server;}

    public List<String> getProperties(){return properties;}
    public void setProperties(List<String> properties){this.properties = properties;}
}
