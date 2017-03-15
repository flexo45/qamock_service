package main.java.mysqlconnector.qa.smppclient.session.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.Pdu;
import main.java.mysqlconnector.qa.smppclient.session.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageListener.class);

    @Override
    public void received(Pdu pdu) {
        logger.debug("{} received, but no session listener set.", pdu.getClass().getName());
    }

}
