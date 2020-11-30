package org.qamock.xml.object;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "email")
public class Email {

    private String server;
    private From from;
    private String to;
    private String subject;
    private String message;

    public Email(){}

    public Email(String server, From from, String to, String subject, String message){
        this.server = server;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    @XmlAttribute(name = "server")
    public String getServer(){return this.server;}
    public void setServer(String server){this.server = server;}

    @XmlElement(name = "from")
    public From getFrom(){return this.from;}
    public void setFrom(From from){this.from = from;}

    @XmlElement(name = "to", required = true)
    public String getTo(){return this.to;}
    public void setTo(String to){this.to = to;}

    @XmlElement(name = "subject", required = true)
    public String getSubject(){return this.subject;}
    public void setSubject(String subject){this.subject = subject;}

    @XmlElement(name = "message", required = true)
    public String getMessage(){return this.message;}
    public void setMessage(String message){this.message = message;}

    @Override
    public String toString(){
        return "Email{server=" + server +
                ", from=" + from +
                ", to=" + to +
                ", subject=" + subject +
                ", message=" + message + "}";
    }

}
