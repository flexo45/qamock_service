package org.qamock.xml.object;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "extract")
public class Extractor {

    private String targetName;
    private String value;
    private String to;

    public Extractor(){}

    public Extractor(String targetName, String value, String to){
        this.targetName = targetName;
        this.value = value;
        this.to = to;
    }

    @XmlAttribute(name = "target-name")
    public String getTargetName(){return targetName;}
    public void setTargetName(String targetName){this.targetName = targetName;}

    @XmlAttribute(name = "value")
    public String getValue(){return value;}
    public void setValue(String value){this.value = value;}

    @XmlAttribute(name = "to")
    public String getTo(){return to;}
    public void setTo(String to){this.to = to;}

}
