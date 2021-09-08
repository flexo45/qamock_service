package org.qamock.app.main.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "connections", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "name"}))
public class Connection implements Serializable {

    private static final long serialVersionUID = -4564564537763765765L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "name", unique = true, nullable = false)
    private String name;
    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    @Column(name = "type")
    private int type;
    public int getType(){return type;}
    public void setType(int type){this.type = type;}

    @Column(name = "url")
    private String url;
    public String getUrl(){return url;}
    public void setUrl(String url){this.url = url;}

    @Column(name = "login")
    private String login;
    public String getLogin(){return login;}
    public void setLogin(String login){this.login = login;}

    @Column(name = "password")
    private String password;
    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    public Connection(){}

    public Connection(long id, String name, String url, String password){
        setId(id);
        setName(name);
        setUrl(url);
        setPassword(password);
    }

    public Connection(String name, String url, String password){
        setId(-1);
        setName(name);
        setUrl(url);
        setPassword(password);
    }

    @Override
    public String toString(){
        return "Connection{id=}";
    }

}
