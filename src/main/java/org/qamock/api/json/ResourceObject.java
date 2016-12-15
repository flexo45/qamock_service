package org.qamock.api.json;

import java.io.Serializable;
import java.util.List;

public class ResourceObject implements Serializable {

    private static final long serialVersionUID = 4765235423543565658L;

    private long id;
    private String path;
    private int strategy;
    private long default_resp;
    private List<String> methods;
    private String script;
    private int logging;

    public long getId(){return id;}
    public void setId(long id){this.id = id;}

    public String getPath(){return path;}
    public void setPath(String path){this.path = path;}

    public int getStrategy(){return strategy;}
    public void setStrategy(int strategy){this.strategy = strategy;}

    public long getDefault_resp(){return default_resp;}
    public void setDefault_resp(long default_resp){this.default_resp = default_resp;}

    public List<String> getMethods(){return methods;}
    public void setMethods(List<String> methods){this.methods = methods;}

    public String getScript(){return script;}
    public void setScript(String script){this.script = script;}

    public int getLogging(){return logging;}
    public void setLogging(int logging){this.logging = logging;}

    @Override
    public String toString(){
        return "ResourceObject{path=" + path +
                ", strategy=" + strategy +
                ", default_resp=" + default_resp +
                ", methods=" + methods +
                ", script=" + script +
                ", logging=" + logging +
                "}";
    }
}
