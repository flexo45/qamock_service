package org.qamock.app.main.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dynamic_resource_methods")
public class DynamicResourceMethod implements Serializable{

    private static final long serialVersionUID = -4758423523462523747L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id) {this.id = id;}

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private DynamicResource dynamicResource;
    public DynamicResource getDynamicResource(){return dynamicResource;}
    public void setDynamicResource(DynamicResource dynamicResource){this.dynamicResource = dynamicResource;}

    @Column(name = "method", nullable = false)
    private String method;
    public String getMethod(){return method;}
    public void setMethod(String method){this.method = method;}

    public DynamicResourceMethod(){}

    public DynamicResourceMethod(long id, DynamicResource dynamicResource, String method){
        this.setId(id);
        this.setDynamicResource(dynamicResource);
        this.setMethod(method);
    }

    public DynamicResourceMethod(DynamicResource dynamicResource, String method){
        this.setId(-1);
        this.setDynamicResource(dynamicResource);
        this.setMethod(method);
    }

    @Override
    public String toString(){
        return "DynamicResourceMethod{id=" + id +
                ", resource_id=" + dynamicResource +
                ", method=" + method + "}";
    }

}
