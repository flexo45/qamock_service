package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sequence")
public class Sequence implements Serializable{

    private static final long serialVersionUID = -4563643657645567657L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    @Column(name = "value", nullable = false)
    private long value;
    public long getValue(){return value;}
    public void setValue(long value){this.value = value;}

    public Sequence(){}

    public Sequence(long id, String name, long value){
        this.setId(id);
        this.setName(name);
        this.setValue(value);
    }

    public Sequence(String name, long value){
        this.setId(-1);
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public String toString(){
        return "Sequence{id=" + id +
                ", name=" + name +
                ", value=" + value + "}";
    }

}
