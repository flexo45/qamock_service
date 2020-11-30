package org.qamock.jms.creator;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class TextMessageCreator implements MessageCreator {

    public TextMessageCreator(@NotNull String text, Map<String, String> properties){
        this.text = text;
        this.properties = properties;
    }

    private String text;

    private Map<String, String> properties;

    @Override
    public Message createMessage(Session session) throws JMSException {

        Message message = session.createTextMessage(text);

        for(Map.Entry<String, String> prop : properties.entrySet()){
            message.setStringProperty(prop.getKey(), prop.getValue());
        }

        return message;
    }
}
