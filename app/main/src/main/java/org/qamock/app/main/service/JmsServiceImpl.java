package org.qamock.app.main.service;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.Queue;
import com.sun.messaging.QueueConnectionFactory;
import org.qamock.app.main.jms.creator.ObjectMessageCreator;
import org.qamock.app.main.jms.creator.TextMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class JmsServiceImpl implements JmsService {

    private static final Logger logger = LoggerFactory.getLogger(JmsServiceImpl.class);

    /*@Autowired
    @Qualifier("OpenMQ")
    JmsTemplate jmsTemplateOpenMq;*/

    /*@Autowired
    QueueConnectionFactory connectionFactoryGlassFish;*/

    /*@Autowired
    CachingConnectionFactory connectionFactoryRabbitMQ;*/

    @Override
    public void sendTextMessage(String address, String destination, String text, Map<String, String> properties) throws JMSException {

        logger.info("Receive text message to send");

        processMQ(address, destination, text, properties);

    }

    @Override
    public void sendObjectMessage(String address, String destination, Serializable object, Map<String, String> properties) throws JMSException {

        logger.info("Receive object message to send: object=" + object.toString());

        processMQ(address, destination, object, properties);
    }

    @Override
    public void sendJmsMessageSerialized(String address, String destination, String object, Map<String, String> properties) throws JMSException {

        Object o = null;

        try {
            byte[] bytes = stringToByteArray(object);
            o = objectFromBytes(bytes);
        }
        catch (Exception e){
            JMSException je = new JMSException("Error on deserialize object");
            je.setLinkedException(e);
            throw je;
        }

        logger.info("Receive universal serialized jms message to send: " + object);

        if(address.contains("amqp://")){
            processAMQP(address, destination, o);
        }
        else if(address.contains("mq://")){
            processMQ(address, destination, o, properties);
        }
        else {
            throw new JMSException("Invalid JMS implementation");
        }
    }

    @Override
    public void sendJmsMessage(String address, String destination, Object object, Map<String, String> properties) throws JMSException {

        logger.info("Receive universal jms message to send: " + object);

        if(address.contains("amqp://")){
            processAMQP(address, destination, object);
        }
        else if(address.contains("mq://")){
            processMQ(address, destination, object, properties);
        }
        else {
            throw new JMSException("Invalid JMS implementation");
        }

    }

    @Cacheable("jmstemplatemq")
    private JmsTemplate getJmsTemplate(ConnectionFactory connectionFactory){
        return new JmsTemplate(connectionFactory);
    }

    @Cacheable("jmstemplateamqp")
    private AmqpTemplate getAmqpTemplate(CachingConnectionFactory connectionFactory){
        return new RabbitTemplate(connectionFactory);
    }

    @Cacheable("connfactorymq")
    private QueueConnectionFactory getConnectionFactoryGlassFish(String addresses) throws JMSException{
        addresses = addresses.contains("mq://") ? addresses.replace("mq://", "") : addresses;
        QueueConnectionFactory queueConnectionFactory = new QueueConnectionFactory();
        queueConnectionFactory.setProperty(ConnectionConfiguration.imqAddressList, addresses);
        return queueConnectionFactory;
    }

    @Cacheable("connfactoryamqp")
    private CachingConnectionFactory getConnectionFactoryRabbitMq(String address) throws JMSException{
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        URI url = null;
        try {
            url = new URI(address);
        }
        catch (URISyntaxException urise){
            JMSException e = new JMSException("Invalid address");
            e.setLinkedException(urise);
            throw e;
        }

        String host = url.getHost();
        int port = url.getPort() == -1 ? 5672 : url.getPort();
        String user = null;
        String password = null;
        if(url.getUserInfo() != null){
            if(url.getUserInfo().contains(":")){
                user = url.getUserInfo().split(":")[0];
                password = url.getUserInfo().split(":")[1];
            }
            else {
                user = url.getUserInfo();
            }
        }

        cachingConnectionFactory.setHost(host);
        if(url.getPort() > 0){cachingConnectionFactory.setPort(port);}
        cachingConnectionFactory.setUsername(user);
        cachingConnectionFactory.setPassword(password);

        return cachingConnectionFactory;
    }

    private void processAMQP(String address, String destination, Object message) throws JMSException{

        AmqpTemplate amqpTemplate = getAmqpTemplate(getConnectionFactoryRabbitMq(address));

        try{
            amqpTemplate.convertAndSend(destination, message);
        }
        catch (AmqpException amqpe){
            logger.error(amqpe.getMessage(), amqpe);
            JMSException jmse = new JMSException("Error on send AMQP message");
            jmse.setLinkedException(amqpe);
            throw  jmse;
        }
    }

    private void processMQ(String address, String destination, Object message, Map<String, String> properties) throws JMSException{

        JmsTemplate jmsTemplate = getJmsTemplate(getConnectionFactoryGlassFish(address));

        if(message.getClass().isInstance("")){
            jmsTemplate.send(new Queue(destination), new TextMessageCreator((String) message, properties));
        }
        else {
            jmsTemplate.send(new Queue(destination), new ObjectMessageCreator((Serializable) message, properties));
        }
    }

    private byte[] stringToByteArray(String object){
        String[] byteValues = object.substring(1, object.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];
        for(int i = 0, len = bytes.length; i < len; i++){
            bytes[i] = Byte.parseByte(byteValues[i].trim());
        }
        return bytes;
    }

    private Object objectFromBytes(byte[] bytes) throws ClassNotFoundException, IOException{
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(byteArrayInputStream);
        return in.readObject();
    }

    private byte[] objectToSend(Object object) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(object);
        out.flush();
        return bos.toByteArray();
    }
}
