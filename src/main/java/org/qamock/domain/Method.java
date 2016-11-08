package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "methods", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Method implements Serializable {
  /*  private static final long serialVersionUID = -8253572378357676245L;

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


    @OneToMany(mappedBy = "method")
    private Set<DynamicResource> dynamicResources = new HashSet<>(0);
    public Set<DynamicResource> getDynamicResources(){
        return this.dynamicResources;
    }
    public void setDynamicResources(Set<DynamicResource> dynamicResources){
        this.dynamicResources = dynamicResources;
    }

    public Method() {}

    public Method(long id, String name){
        this.setId(id);
        this.setName(name);
    }

    public Method(String name){
        this.setId(-1);
        this.setName(name);
    }

    public Method(String name, Set<DynamicResource> dynamicResources){
        this.setId(-1);
        this.setName(name);
        this.setDynamicResources(dynamicResources);
    }

    @Override
    public String toString(){
        return "Method{id=" + id + ", name=" + name + "}";
    }*/
}
