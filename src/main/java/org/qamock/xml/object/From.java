package org.qamock.xml.object;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "from")
public class From {

    private String personAddress;
    private String fromAddress;

    public From(){}

    public From(String personAddress, String fromAddress){
        this.personAddress = personAddress;
        this.fromAddress = fromAddress;
    }

    @XmlAttribute(name = "person")
    public String getPersonAddress(){return this.personAddress;}
    public void setPersonAddress(String personAddress){this.personAddress = personAddress;}

    @XmlValue
    public String getFromAddress(){return this.fromAddress;}
    public void setFromAddress(String fromAddress){this.fromAddress = fromAddress;}

    @Override
    public String toString(){
        return "From{personAddress=" + personAddress +
                ", fromAddress=" + fromAddress + "}";
    }

}
