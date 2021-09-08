package org.qamock.app.main.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "headers", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "dynamic_response_id"}))
public class Header implements Serializable {
    private static final long serialVersionUID = -1110956792566573438L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "name", nullable = false)
    private String name;
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    @Column(name = "value", nullable = false)
    private String value;
    public String getValue(){return this.value;}
    public void setValue(String value){this.value = value;}

    @ManyToOne
    @JoinColumn(name = "dynamic_response_id", nullable = false)
    private DynamicResponse dynamicResponse;
    public DynamicResponse getDynamicResponse(){return this.dynamicResponse;}
    public void setDynamicResponse(DynamicResponse dynamicResponse){this.dynamicResponse = dynamicResponse;}

    public Header() {}

    public Header(long id, String name, String value, DynamicResponse response){
        setId(id);
        setName(name);
        setValue(value);
        setDynamicResponse(response);
    }

    public Header(String name, String value, DynamicResponse response){
        setId(-1);
        setName(name);
        setValue(value);
        setDynamicResponse(response);
    }

    @Override
    public boolean equals(Object o){
        if(o == null) {return false;}
        if(o instanceof Header){
            Header other = (Header) o;
            return other.getName().equals(this.getName())
                    && other.getDynamicResponse().getId() == this.getDynamicResponse().getId();
        }
        else {
            return false;
        }
    }

    @Override
    public String toString(){
        return "Header{id=" + id + ", name=" + name + ", value=" + value + "}";
    }
}