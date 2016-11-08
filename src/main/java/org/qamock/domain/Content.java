package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "content", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "dynamic_response_id"}))
public class Content implements Serializable {
    private static final long serialVersionUID = -1110956792566573438L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "text", nullable = false, length = 20480)
    private String text;
    public String getText(){return this.text;}
    public void setText(String text){this.text = text;}

    @ManyToOne
    @JoinColumn(name = "dynamic_response_id", nullable = false)
    private DynamicResponse dynamicResponse;
    public DynamicResponse getDynamicResponse(){return this.dynamicResponse;}
    public void setDynamicResponse(DynamicResponse dynamicResponse){this.dynamicResponse = dynamicResponse;}

    public Content(){}

    public Content(long id, String text, DynamicResponse dynamicResponse){
        setId(id);
        setText(text);
        setDynamicResponse(dynamicResponse);
    }

    public Content(String text, DynamicResponse dynamicResponse){
        setId(-1);
        setText(text);
        setDynamicResponse(dynamicResponse);
    }

    @Override
    public String toString(){
        return "Content{id=" + id + ", text=" + text + ", dynamic_response_id=" + dynamicResponse + "}";
    }

}
