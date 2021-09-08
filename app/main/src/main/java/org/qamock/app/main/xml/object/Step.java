package org.qamock.app.main.xml.object;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Step {

    private int id;
    private String type;
    private long value;

    private String method;
    private String path;
    private String query;
    private List<Header> headers;
    private String content;

    private Statement statement;
    private List<String> params;
    private String connection;

    private List<Extractor> extractors;

    public Step(){}

    public Step(int id,
                String type,
                long value,
                String method,
                String path,
                String query,
                List<Header> headers,
                String content,
                Statement statement,
                List<String> params,
                String connection,
                List<Extractor> extractors){

        this.id = id;
        this.type = type;
        this.value = value;

        this.method = method;
        this.path = path;
        this.query = query;
        this.headers = headers;
        this.content = content;
        this.statement = statement;
        this.params = params;
        this.connection = connection;;
        this.extractors = extractors;
    }

    @XmlAttribute(name = "id", required = true)
    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    @XmlAttribute(name = "type", required = true)
    public String getType(){return type;}
    public void setType(String type){this.type = type;}

    @XmlAttribute(name = "value")
    public long getValue(){return value;}
    public void setValue(long value){this.value = value;}

    @XmlAttribute(name = "connection")
    public String getConnection(){return connection;}
    public void setConnection(String connection){this.connection = connection;}

    @XmlElement(name = "method")
    public String getMethod(){return method;}
    public void setMethod(String method){this.method = method;}

    @XmlElement(name = "path")
    public String getPath(){return path;}
    public void setPath(String path){this.path = path;}

    @XmlElement(name = "query")
    public String getQuery(){return query;}
    public void setQuery(String query){this.query = query;}

    @XmlElementWrapper(name = "headers")
    @XmlElement(name = "header", type = Header.class)
    public List<Header> getHeaders(){return headers;}
    public void setHeaders(List<Header> headers){this.headers = headers;}

    @XmlElement(name = "content")
    public String getContent(){return content;}
    public void setContent(String content){this.content = content;}

    @XmlElement(name = "statement")
    public Statement getStatement(){return statement;}
    public void setStatement(Statement statement){this.statement = statement;}

    @XmlElementWrapper(name = "params")
    @XmlElement(name = "param", type = String.class)
    public List<String> getParams(){return params;}
    public void setParams(List<String> params){this.params = params;}

    @XmlElementWrapper(name = "extractors")
    @XmlElement(name = "extract", type = Extractor.class)
    public List<Extractor> getExtractors(){return extractors;}
    public void setExtractors(List<Extractor> extractors){this.extractors = extractors;}

}
