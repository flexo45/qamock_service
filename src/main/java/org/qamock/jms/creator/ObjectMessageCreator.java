package org.qamock.jms.creator;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

public class ObjectMessageCreator implements MessageCreator {

    public ObjectMessageCreator(@NotNull Serializable object, Map<String, String> properties){
        this.object = object;
        this.properties = properties;
    }

    private Serializable object;

    private Map<String, String> properties;

    @Override
    public Message createMessage(Session session) throws JMSException {

        Message message = session.createObjectMessage(object);

        for(Map.Entry<String, String> prop : properties.entrySet()){
            message.setStringProperty(prop.getKey(), prop.getValue());
        }

        return message;
    }
}
