package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dynamic_resources", uniqueConstraints = @UniqueConstraint(columnNames = "path"))
public class DynamicResource implements Serializable {

    private static final long serialVersionUID = -4537609702358236547L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "path", unique = true)
    private String path;
    public String getPath() {return path;}
    public void setPath(String path) {this.path = path;}

    /*
    @ManyToOne
    @JoinColumn(name = "method_id", nullable = false)
    private Method method;
    public Method getMethod() {return method;}
    public void setMethod(Method method) {this.method = method;}*/

    /*0-seq, 1-rnd, 2-script */
    @Column(name = "dispatch_strategy", nullable = false)
    private int dispatch_strategy;
    public int getDispatch_strategy() {return dispatch_strategy;}
    public void setDispatch_strategy(int dispatch_strategy) {this.dispatch_strategy = dispatch_strategy;}

    @ManyToOne
    @JoinColumn(name = "last_response_id")
    private DynamicResponse lastDynamicResponse;
    public DynamicResponse getLastDynamicResponse() {return lastDynamicResponse;}
    public void setLastDynamicResponse(DynamicResponse lastDynamicResponse) {
        this.lastDynamicResponse = lastDynamicResponse;
    }

    @ManyToOne
    @JoinColumn(name = "default_response_id")
    private DynamicResponse defaultDynamicResponse;
    public DynamicResponse getDefaultDynamicResponse() {return defaultDynamicResponse;}
    public void setDefaultDynamicResponse(DynamicResponse defaultDynamicResponse) {
        this.defaultDynamicResponse = defaultDynamicResponse;
    }

    public DynamicResource(){}

    public DynamicResource(long id, String path, int dispatch_strategy, DynamicResponse lastDynamicResponse
            , DynamicResponse defaultDynamicResponse){
        this.setId(id);
        this.setPath(path);
        this.setDispatch_strategy(dispatch_strategy);
        this.setLastDynamicResponse(lastDynamicResponse);
        this.setDefaultDynamicResponse(defaultDynamicResponse);
    }

    public DynamicResource(String path, int dispatch_strategy_id, DynamicResponse lastDynamicResponse
            , DynamicResponse defaultDynamicResponse){
        this.setId(-1);
        this.setPath(path);
        this.setDispatch_strategy(dispatch_strategy_id);
        this.setLastDynamicResponse(lastDynamicResponse);
        this.setDefaultDynamicResponse(defaultDynamicResponse);
    }

    @Override
    public String toString(){
        return "DynamicResource{id=" + id +
                ", path=" + path +
                ", dispatch_strategy=" + dispatch_strategy +
                ", default_response_id=" + defaultDynamicResponse + "}";
    }

}
