package org.qamock.app.main.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mock_request")
public class MockRequest implements Serializable {

    private static final long serialVersionUID = -5436345765767457647L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "method", nullable = false)
    private String method;
    public String getMethod() {return method;}
    public void setMethod(String method) {this.method = method;}

    @Column(name = "path", nullable = false)
    private String path;
    public String getPath() {return path;}
    public void setPath(String path) {this.path = path;}

    @Column(name = "headers", length=20480)
    private String headers;
    public String getHeaders() {return headers;}
    public void setHeaders(String headers) {this.headers = headers;}

    @Column(name = "content", length=20480)
    private String content;
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public MockRequest(){}

    public MockRequest(long id, String method, String path, String headers, String content){
        this.setId(id);
        this.setMethod(method);
        this.setPath(path);
        this.setHeaders(headers);
        this.setContent(content);
    }

    public MockRequest(String method, String path, String headers, String content){
        this.setId(-1);
        this.setMethod(method);
        this.setPath(path);
        this.setHeaders(headers);
        this.setContent(content);
    }

    @Override
    public String toString(){
        return "MockRequest{id=" + id +
                ", method=" + method +
                ", path=" + path +
                ", headers=" + headers +
                ", content=" + content + "}";
    }

}
