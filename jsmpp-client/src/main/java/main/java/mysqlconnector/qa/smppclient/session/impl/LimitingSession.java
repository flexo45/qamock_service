package main.java.mysqlconnector.qa.smppclient.session.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.Pdu;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.pdu.impl.SubmitSm;
import main.java.mysqlconnector.qa.smppclient.session.MessageListener;
import main.java.mysqlconnector.qa.smppclient.session.Session;
import main.java.mysqlconnector.qa.smppclient.session.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class LimitingSession implements Session {

    private static final Logger logger = LoggerFactory.getLogger(LimitingSession.class);

    /**
     * Limit message count per this amount of time.
     */
    private static final long nano = 1000*1000;
    private static final long PERIOD = 1010*nano;

    private final Session session;
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Holds the times when the last messages were sent.
     */
    private final long[] sentTimes;
    /**
     * Index of beginning of queue
     */
    private int sentTimeIdx;

    public LimitingSession(Session session, int maxMessagesPerSecond) {
        this.session = session;
        this.sentTimes = new long[maxMessagesPerSecond];
    }

    @Override
    public void setMessageListener(MessageListener messageListener) {
        session.setMessageListener(messageListener);
    }

    @Override
    public void setStateListener(StateListener stateListener) {
        session.setStateListener(stateListener);
    }

    @Override
    public MessageListener getMessageListener() {
        return session.getMessageListener();
    }

    @Override
    public void setSmscResponseTimeout(int timeout) {
        session.setSmscResponseTimeout(timeout);
    }

    @Override
    public void setEnquireLinkTimeout(int timeout) {
        session.setEnquireLinkTimeout(timeout);
    }

    @Override
    public void setReconnectTimeout(int timeout) {
        session.setReconnectTimeout(timeout);
    }

    @Override
    public Pdu open(Pdu pdu) throws PduException, IOException {
        return session.open(pdu);
    }

    @Override
    public long nextSequenceNumber() {
        return session.nextSequenceNumber();
    }

    @Override
    public boolean send(Pdu pdu) throws PduException
    {
        if (CommandId.SUBMIT_SM != pdu.getCommandId())
        {
            return session.send(pdu);
        }
        else
        {
            lock.lock();
            try
            {
                //sleep for (last_time + period) - now
                long timeToSleep = (sentTimes[sentTimeIdx] - System.nanoTime() + PERIOD);
                if (timeToSleep > 0)
                {
                    long whole = timeToSleep / nano; //in millis
                    long rest = timeToSleep - (whole * nano);//remainder of division
                    if (rest > 0)
                    {
                        whole++;//just to round to whole millis
                    }
                    logger.trace("Wait before send: {} ms.", whole);
                    Thread.sleep(whole);
                }
                return session.send(pdu);
            }
            catch (InterruptedException e)
            {
                logger.warn("Send interrupted.", e);
                return false;
            }
            finally
            {
                sentTimes[sentTimeIdx] = System.nanoTime();
                sentTimeIdx++;
                sentTimeIdx %= sentTimes.length;
                lock.unlock();
            }
        }
    }

    @Override
    public void close() {
        session.close();
    }

    public static void main(String[] args) throws PduException
    {
        final LimitingSession session = new LimitingSession(new Session()
        {

            @Override
            public void setMessageListener(MessageListener messageListener)
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public MessageListener getMessageListener()
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setStateListener(StateListener stateListener)
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setSmscResponseTimeout(int timeout)
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setEnquireLinkTimeout(int timeout)
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setReconnectTimeout(int timeout)
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Pdu open(Pdu pdu) throws PduException, IOException
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public long nextSequenceNumber()
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean send(Pdu pdu) throws PduException
            {
                System.out.println(Calendar.getInstance().getTime());
                return true;
            }

            @Override
            public void close()
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }, 100);
        ExecutorService service = Executors.newFixedThreadPool(5);
        Runnable r = new Runnable()
        {
            public void run()
            {
                try{
                    session.send(new SubmitSm());
                }catch(Exception e){
                    logger.error("", e);
                }
            }
        };
        for (int i = 0; i < 10000; i++)
        {
            service.submit(r);
        }
    }

}
