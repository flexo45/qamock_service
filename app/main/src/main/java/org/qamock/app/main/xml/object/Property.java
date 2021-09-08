package org.qamock.app.main.xml.object;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "property")
public class Property {

    private String key;
    private String value;

    public Property(){}

    public Property(String key, String value){
        this.key = key;
        this.value = value;
    }

    @XmlAttribute(name = "key")
    public String getKey(){return key;}
    public void setKey(String key){this.key = key;}

    @XmlAttribute(name = "value")
    public String getValue(){return value;}
    public void setValue(String value){this.value = value;}

}
