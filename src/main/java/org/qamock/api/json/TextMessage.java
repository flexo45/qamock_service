package org.qamock.api.json;

import java.io.Serializable;
import java.util.List;

public class TextMessage implements Serializable {

    private static final long serialVersionUID = 1513207428686438208L;

    private String text;
    private String queue;
    private String server;
    private List<String> properties;

    public String getText(){return text;}
    public void setText(String text){this.text = text;}

    public String getQueue(){return queue;}
    public void setQueue(String queue){this.queue = queue;}

    public String getServer(){return server;}
    public void setServer(String server){this.server = server;}

    public List<String> getProperties(){return properties;}
    public void setProperties(List<String> properties){this.properties = properties;}
}
