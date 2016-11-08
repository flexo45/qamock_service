package org.qamock.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "scripts")
public class Script implements Serializable {
    private static final long serialVersionUID = -7654652935755934654L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "text", length = 20480)
    private String text;
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    @JoinColumn(name = "dynamic_resource_id", nullable = true)
    private DynamicResource dynamicResource;
    public DynamicResource getDynamicResource() {return dynamicResource;}
    public void setDynamicResource(DynamicResource resource) {this.dynamicResource = resource;}

    @JoinColumn(name = "dynamic_response_id", nullable = true)
    private DynamicResponse dynamicResponse;
    public DynamicResponse getDynamicResponse() {return dynamicResponse;}
    public void setDynamicResponse(DynamicResponse dynamicResponse) {this.dynamicResponse = dynamicResponse;}

    public Script() {}

    public Script(long id, String text, DynamicResource resource, DynamicResponse response){
        this.setId(id);
        this.setText(text);
        this.setDynamicResource(resource);
        this.setDynamicResponse(response);
    }

    public Script(String text, DynamicResource resource, DynamicResponse response){
        this.setId(-1);
        this.setText(text);
        this.setDynamicResource(resource);
        this.setDynamicResponse(response);
    }

    @Override
    public String toString(){
        return "Script{text=" + text +
                ", dynamic_resource_id=" + dynamicResource +
                ", dynamic_response_id=" + dynamicResponse + "}";
    }
}