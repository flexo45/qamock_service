package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "script_suite", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "name"}))
public class ScriptSuite implements Serializable {
    private static final long serialVersionUID = -1110956792566573438L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id) {this.id = id;}

    @Column(name ="name", unique = true, nullable = false, length = 1024)
    private String name;
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    @Column(name = "text", nullable = false, length = 20480)
    private String text;
    public String getText(){return this.text;}
    public void setText(String text){this.text = text;}

    public ScriptSuite(){}

    public ScriptSuite(long id, String name, String text){
        setId(id);
        setName(name);
        setText(text);
    }

    public ScriptSuite(String name, String text){
        setId(-1);
        setName(name);
        setText(text);
    }

    @Override
    public String toString(){
        return "Content{id=" + id + ", name=" + name + ", text=" + text + "}";
    }

}
