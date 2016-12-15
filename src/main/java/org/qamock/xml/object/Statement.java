package org.qamock.xml.object;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "statement")
public class Statement {

    private String type;
    private String query;

    public Statement(){}

    public Statement(String type, String query){
        this.type = type;
        this.query = query;
    }

    @XmlElement(name = "type")
    public String getType(){return type;}
    public void setType(String type){this.type = type;}

    @XmlElement(name = "query")
    public String getQuery(){return query;}
    public void setQuery(String query){this.query = query;}

}
