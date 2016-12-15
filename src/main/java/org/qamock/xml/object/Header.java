package org.qamock.xml.object;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "header")
public class Header {

    private String key;
    private String value;

    public Header(){}

    public Header(String key, String value){
        this.key = key;
        this.value = value;
    }

    @XmlElement(name = "key")
    public String getKey(){return key;}
    public void setKey(String key){this.key = key;}

    @XmlElement(name = "val")
    public String getValue(){return value;}
    public void setValue(String value){this.value = value;}

}
