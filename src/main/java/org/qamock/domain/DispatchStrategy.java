package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "dispatch_strategies", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class DispatchStrategy implements Serializable {
    /*private static final long serialVersionUID = -6745345756568564853L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "name", unique = true)
    private String name;
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    @OneToMany(mappedBy = "dispatch_strategy")
    private Set<DynamicResource> dynamicResources = new HashSet<>(0);
    public Set<DynamicResource> getDynamicResources(){
        return this.dynamicResources;
    }
    public void setDynamicResources(Set<DynamicResource> dynamicResources){
        this.dynamicResources = dynamicResources;
    }

    public DispatchStrategy() {}

    public DispatchStrategy(long id, String name){
        this.setId(id);
        this.setName(name);
    }

    public DispatchStrategy(String name){
        this.setId(-1);
        this.setName(name);
    }

    public DispatchStrategy(String name, Set<DynamicResource> dynamicResources){
        this.setId(-1);
        this.setName(name);
        this.setDynamicResources(dynamicResources);
    }

    @Override
    public String toString(){
        return "DispatchStrategy{id=" + id + ", name=" + name + "}";
    }*/
}