package main.java.mysqlconnector.qa.smppclient.session;

import main.java.mysqlconnector.qa.smppclient.pdu.Pdu;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;

import java.io.IOException;

public interface Session {

    /**
     * Wait 30 seconds for response by default.
     */
    static final int DEFAULT_SMSC_RESPONSE_TIMEOUT = 30000;

    /**
     * Send ENQUIRE_LINK requests every 30 seconds to check SMSC alive.
     */
    static final int DEFAULT_ENQUIRE_LINK_TIMEOUT = 30000;

    /**
     * Try to reconnect every N ms.
     */
    static final int DEFAULT_RECONNECT_TIMEOUT = 1000;

    /**
     * Set incoming messages from SMSC listener.
     *
     * @param messageListener incoming message listener
     */
    void setMessageListener(MessageListener messageListener);


    /*
     * return Message Listener
     * */
    public MessageListener getMessageListener();


    /**
     * Set session state listener
     *
     * @param stateListener state listener
     */
    void setStateListener(StateListener stateListener);

    /**
     * Set time in ms in which SMSC should response.
     *
     * @param timeout time in milliseconds
     */
    void setSmscResponseTimeout(int timeout);

    /**
     * Session inactivity time in milliseconds after that ENQUIRE_LINK should be sent,
     * to check SMSC availability.
     *
     * @param timeout time in milliseconds
     */
    void setEnquireLinkTimeout(int timeout);

    /**
     * On IO failure try to reconnect to SMSC every timeout milliseconds.
     *
     * @param timeout   time in milliseconds
     */
    void setReconnectTimeout(int timeout);

    /**
     * Open session. Establish TCP connection and send provided bind PDU.
     * Sequence number assigned automatically.
     *
     * @param pdu bind request
     * @return bind response
     * @throws PduException PDU parsing failed
     * @throws IOException  input-output exception
     */
    Pdu open(Pdu pdu) throws PduException, IOException;

    /**
     * Unique PDU sequence number, used to track SMSC response.
     * Application should first call this method, then set returned sequence number to PDU and
     * send it.
     *
     * @return relatively unique PDU sequence number
     */
    long nextSequenceNumber();

    /**
     * Send PDU to SMSC.
     *
     * @param pdu pdu to send
     * @return send successful
     * @throws PduException PDU parsing failed
     */
    boolean send(Pdu pdu) throws PduException;

    /**
     * Send Unbind request, wait for UnbindResp, then close TCP connection and free all resources.
     * Session may be reopened.
     */
    void close();

}
