package org.qamock.app.main.xml.object;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "suite")
public class Suite {

    private List<Property> properties;

    private List<Step> steps;

    public Suite(){}

    public Suite(List<Property> properties, List<Step> steps){
        this.properties = properties;
        this.steps = steps;
    }

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property", type = Property.class)
    public List<Property> getProperties(){return properties;}
    public void setProperties(List<Property> properties){this.properties = properties;}

    @XmlElementWrapper(name = "steps")
    @XmlElement(name = "step", type = Step.class)
    public List<Step> getSteps(){return steps;}
    public void setSteps(List<Step> steps){this.steps = steps;}

}
