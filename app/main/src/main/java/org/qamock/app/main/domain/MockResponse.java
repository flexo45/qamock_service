package org.qamock.app.main.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mock_response")
public class MockResponse implements Serializable {

    private static final long serialVersionUID = -5436345765767457647L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "headers", length=20480)
    private String headers;
    public String getHeaders() {return headers;}
    public void setHeaders(String headers) {this.headers = headers;}

    @Column(name = "content", length=20480)
    private String content;
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public MockResponse(){}

    public MockResponse(long id, String headers, String content){
        this.setId(id);
        this.setHeaders(headers);
        this.setContent(content);
    }

    public MockResponse(String headers, String content){
        this.setId(-1);
        this.setHeaders(headers);
        this.setContent(content);
    }

    @Override
    public String toString(){
        return "MockRequest{id=" + id +
                ", headers=" + headers +
                ", content=" + content + "}";
    }

}
