package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dynamic_responses", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "dynamic_resource_id"}))
public class DynamicResponse implements Serializable {

    private static final long serialVersionUID = -2645728657104785774L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id){this.id = id;}

    @Column(name = "name", nullable = false)
    private String name;
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    @Column(name = "code", nullable = false)
    private int code;
    public int getCode(){return this.code;}
    public void setCode(int code){this.code = code;}

    @ManyToOne
    @JoinColumn(name = "dynamic_resource_id", nullable = false)
    private DynamicResource dynamicResource;
    public DynamicResource getDynamicResource(){return this.dynamicResource;}
    public void setDynamicResource(DynamicResource dynamicResource) {this.dynamicResource = dynamicResource;}

    public DynamicResponse(){}

    public DynamicResponse(long id, String name, int code, DynamicResource dynamicResource){
        setId(id);
        setName(name);
        setCode(code);
        setDynamicResource(dynamicResource);
    }

    public DynamicResponse(String name, int code, DynamicResource dynamicResource){
        setId(-1);
        setName(name);
        setCode(code);
        setDynamicResource(dynamicResource);
    }

    @Override
    public String toString(){
        return "DynamicResponse{id=" + id +
                ", name=" + name +
                ", code=" + code +
                ", dynamic_resource_id=" + /*dynamicResource +*/ "}"; //TODO NULL POINTER IN SAME CASE
    }
}