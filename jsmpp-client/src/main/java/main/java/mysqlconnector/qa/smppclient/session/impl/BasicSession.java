package main.java.mysqlconnector.qa.smppclient.session.impl;

import main.java.mysqlconnector.qa.smppclient.net.Connection;
import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.CommandStatus;
import main.java.mysqlconnector.qa.smppclient.pdu.Pdu;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.pdu.impl.EnquireLink;
import main.java.mysqlconnector.qa.smppclient.pdu.impl.EnquireLinkResp;
import main.java.mysqlconnector.qa.smppclient.pdu.impl.Unbind;
import main.java.mysqlconnector.qa.smppclient.session.MessageListener;
import main.java.mysqlconnector.qa.smppclient.session.Session;
import main.java.mysqlconnector.qa.smppclient.session.State;
import main.java.mysqlconnector.qa.smppclient.session.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BasicSession implements Session {

    private static final Logger logger = LoggerFactory.getLogger(BasicSession.class);

    private final Connection conn;

    private int smscResponseTimeout = DEFAULT_SMSC_RESPONSE_TIMEOUT;
    private int pingTimeout = DEFAULT_ENQUIRE_LINK_TIMEOUT;
    private int reconnectTimeout = DEFAULT_RECONNECT_TIMEOUT;
    private MessageListener messageListener = new DefaultMessageListener();
    private StateListener stateListener = new DefaultStateListener();
    private PingThread pingThread;
    private ReadThread readThread;

    private Pdu bindPdu;
    private volatile long sequenceNumber = 0;
    private volatile long lastActivity;
    private volatile State state = State.DISCONNECTED;
    private final ReentrantLock lock = new ReentrantLock();

    public BasicSession(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public MessageListener getMessageListener() {
        return messageListener;
    }

    @Override
    public void setStateListener(StateListener stateListener) {
        this.stateListener = stateListener;
    }

    @Override
    public void setSmscResponseTimeout(int timeout) {
        this.smscResponseTimeout = timeout;
    }

    @Override
    public void setEnquireLinkTimeout(int timeout) {
        this.pingTimeout = timeout;
    }

    @Override
    public void setReconnectTimeout(int timeout) {
        this.reconnectTimeout = timeout;
    }

    @Override
    public Pdu open(Pdu pdu) throws PduException, IOException {
        lock.lock();
        try{
            bindPdu = pdu;
            return open();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long nextSequenceNumber() {
        lock.lock();
        try{
            if (sequenceNumber == 2147483647L)
                sequenceNumber = 1;
            else
                sequenceNumber++;
            return sequenceNumber;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean send(Pdu pdu) throws PduException {
        if (State.CONNECTED != state)
            return false;
        lock.lock();
        try {
            conn.write(pdu);
            return true;
        } catch (IOException e) {
            logger.debug("Send failed.", e);
            reconnect(e);
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {
        lock.lock();
        try{
            if (State.RECONNECTING == state || closeInternal(null))
                updateState(State.DISCONNECTED);
        } finally {
            lock.unlock();
        }
    }

    private Pdu open() throws PduException, IOException {
        ScheduledExecutorService es = null;
        lock.lock();
        try {
            logger.trace("Opening new session...");
            conn.open();
            logger.trace("TCP connection established. Sending bind request.");
            bindPdu.setSequenceNumber(nextSequenceNumber());
            conn.write(bindPdu);
            es = Executors.newSingleThreadScheduledExecutor();
            es.schedule(new Runnable() {
                @Override
                public void run() {
                    logger.warn("Bind response timed out.");
                    conn.close();
                }
            }, smscResponseTimeout, TimeUnit.MILLISECONDS);
            logger.trace("Bind request sent. Waiting for bind response.");
            Pdu bindResp = conn.read();
            es.shutdownNow();
            logger.trace("Bind response command status: {}.", bindResp.getCommandStatus());
            if (CommandStatus.ESME_ROK == bindResp.getCommandStatus()) {
                updateLastActivity();
                pingThread = new PingThread();
                pingThread.setName("Ping");
                pingThread.start();
                readThread = new ReadThread();
                Thread t2 = new Thread(readThread);
                t2.setName("Read");
                t2.start();
                updateState(State.CONNECTED);
                logger.trace("Session successfully opened.");
            }
            return bindResp;
        } finally {
            lock.unlock();
            if (es != null && !es.isShutdown())
            {
                es.shutdownNow();
            }
        }
    }

    /**
     * Actually close session.
     *
     * @param reason exception, caused session close, or null
     * @return session closed
     */
    private boolean closeInternal(Exception reason) {
        lock.lock();
        try {
            if (State.DISCONNECTED != state) {
                logger.trace("Closing session...");
                pingThread.stopAndInterrupt();
                pingThread = null;
                if (!(reason instanceof IOException) && readThread.run) {
                    try {
                        synchronized (conn) {
                            Pdu unbind = new Unbind();
                            unbind.setSequenceNumber(nextSequenceNumber());
                            send(unbind);
                            conn.wait(smscResponseTimeout);
                        }
                    } catch (Exception e) {
                        logger.debug("Unbind request send failed.", e);
                    }
                }
                readThread.stop();
                readThread = null;
                conn.close();
                logger.trace("Session closed.");
                return true;
            } else {
                logger.trace("Session already closed.");
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    private void reconnect(Exception reason) {
        // only one thread should do reconnect
        boolean doReconnect = false;
        lock.lock();
        try{
            if (State.RECONNECTING != state) {
                doReconnect = true;
                state = State.RECONNECTING;
            }
        } finally {
            lock.unlock();
        }
        if (doReconnect) {
            closeInternal(reason);
            new Thread(new ReconnectThread(reason)).start();
        }
    }


    private void updateLastActivity() {
        lastActivity = System.currentTimeMillis();
    }

    private void updateState(State newState) {
        updateState(newState, null);
    }

    private void updateState(State newState, Exception e) {
        this.state = newState;
        stateListener.changed(newState, e);
    }

    private class PingThread extends Thread {

        private volatile boolean run = true;

        @Override
        public void run() {
            logger.trace("Ping thread started.");
            try {
                while (run) {
                    logger.trace("Checking last activity.");
                    try {
                        if(pingTimeout <= 0)
                        {
                            Thread.sleep(DEFAULT_ENQUIRE_LINK_TIMEOUT);
                            continue;
                        }
                        Thread.sleep(pingTimeout);
                        if (pingTimeout < (System.currentTimeMillis() - lastActivity)) {
                            long prevLastActivity = lastActivity;
                            Pdu enquireLink = new EnquireLink();
                            enquireLink.setSequenceNumber(nextSequenceNumber());
                            send(enquireLink);
                            synchronized (conn) {
                                conn.wait(smscResponseTimeout);
                            }
                            if (run && lastActivity == prevLastActivity) {
                                reconnect(new IOException("Enquire link response not received. Session closed."));
                            }
                        }
                    } catch (InterruptedException e) {
                        logger.trace("Ping thread interrupted.");
                    }
                }
            } catch (PduException e) {
                if (run) {
                    logger.warn("EnquireLink request failed.", e);
                    run = false;
                    reconnect(e);
                }
            } finally {
                logger.trace("Ping thread stopped.");
            }
        }

        void stopAndInterrupt() {
            run = false;
            interrupt();
        }

    }

    private class ReadThread implements Runnable {

        private volatile boolean run = true;

        @Override
        public void run()
        {
            logger.trace("Read thread started.");
            try
            {
                while (run)
                {
                    Pdu request = conn.read();
                    updateLastActivity();
                    Pdu response;
                    if (CommandId.ENQUIRE_LINK == request.getCommandId())
                    {
                        logger.debug("Received ENQUIRE_LINK with seq_num={}", request.getSequenceNumber());
                        response = new EnquireLinkResp();
                        response.setSequenceNumber(request.getSequenceNumber());
                        send(response);
                        logger.debug("Sent ENQUIRE_LINK_RESP with seq_num={}", response.getSequenceNumber());
                    }
                    else if (CommandId.ENQUIRE_LINK_RESP == request.getCommandId())
                    {
                        synchronized (conn) {
                            conn.notifyAll();
                        }
                    }
                    else if (CommandId.UNBIND_RESP == request.getCommandId())
                    {
                        synchronized (conn) {
                            conn.notifyAll();
                        }
                        stop();
                    }
                    else
                    {
                        messageListener.received(request);
                    }
                }
            }
            catch (PduException e)
            {
                if (run) {
                    logger.warn("Incoming message parsing failed.", e);
                    run = false;
                    reconnect(e);
                }
            }
            catch (IOException e)
            {
                if (run) {
                    logger.warn("Reading IO failure.", e);
                    run = false;
                    reconnect(e);
                }
            }
            catch(Exception e)
            {
                if(run)
                {
                    logger.error("Error occured processing imcomming PDU.", e);
                    run = false;
                    reconnect(e);
                }
            }
            finally
            {
                logger.trace("Read thread stopped.");
            }
        }

        void stop()
        {
            run = false;
        }

    }

    private class ReconnectThread implements Runnable {

        private final Exception reason;

        private ReconnectThread(Exception reason) {
            this.reason = reason;
        }

        @Override
        public void run() {
            logger.debug("Reconnect started.");
            stateListener.changed(state, reason);
            boolean reconnectSuccessful = false;
            while (!reconnectSuccessful && state == State.RECONNECTING) {
                logger.debug("Reconnecting...");
                try {
                    conn.close();
                    Pdu bindResponse = open();
                    if (CommandStatus.ESME_ROK == bindResponse.getCommandStatus()) {
                        reconnectSuccessful = true;
                    } else {
                        logger.warn("Reconnect failed. Bind response error code: {}.",
                                bindResponse.getCommandStatus());
                    }
                } catch (Exception e) {
                    logger.warn("Reconnect failed.", e);
                    try {
                        Thread.sleep(reconnectTimeout);
                    } catch (InterruptedException e1) {
                        logger.trace("Reconnect sleep interrupted.", e1);
                    }
                }
            }
            if (reconnectSuccessful)
            {
                state = State.CONNECTED;
            }
            logger.debug("Reconnect done.");
        }
    }

}
