package org.qamock.service;

import javax.jms.JMSException;
import java.io.Serializable;
import java.util.Map;

public interface JmsService {

    void sendTextMessage(String address, String destination, String text, Map<String, String> properties) throws JMSException;

    void sendObjectMessage(String address, String destination, Serializable object, Map<String, String> properties) throws JMSException;

}
