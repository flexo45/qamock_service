package org.qamock.service;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.Queue;
import com.sun.messaging.QueueConnectionFactory;
import org.qamock.jms.creator.ObjectMessageCreator;
import org.qamock.jms.creator.TextMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.Serializable;
import java.util.Map;

@Service
public class JmsServiceImpl implements JmsService {

    private static final Logger logger = LoggerFactory.getLogger(JmsServiceImpl.class);

    @Autowired
    //@Qualifier("jmsTemplateCachingGF")
    JmsTemplate jmsTemplate;

    @Autowired
    QueueConnectionFactory connectionFactoryGlassFish;

    @Override
    public void sendTextMessage(String address, String destination, String text, Map<String, String> properties) throws JMSException {

        logger.info("Receive text message to send");

        connectionFactoryGlassFish.setProperty(ConnectionConfiguration.imqAddressList, address);
        jmsTemplate.setConnectionFactory(connectionFactoryGlassFish);
        jmsTemplate.send(new Queue(destination), new TextMessageCreator(text, properties));
    }

    @Override
    public void sendObjectMessage(String address, String destination, Serializable object, Map<String, String> properties) throws JMSException {

        logger.info("Receive object message to send: object=" + object.toString());

        connectionFactoryGlassFish.setProperty(ConnectionConfiguration.imqAddressList, address);
        jmsTemplate.setConnectionFactory(connectionFactoryGlassFish);
        jmsTemplate.send(new Queue(destination), new ObjectMessageCreator(object, properties));
    }
}
