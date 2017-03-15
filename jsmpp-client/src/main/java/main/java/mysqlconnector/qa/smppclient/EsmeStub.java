package main.java.mysqlconnector.qa.smppclient;

import main.java.mysqlconnector.qa.smppclient.net.impl.TcpConnection;
import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.CommandStatus;
import main.java.mysqlconnector.qa.smppclient.pdu.Pdu;
import main.java.mysqlconnector.qa.smppclient.pdu.impl.*;
import main.java.mysqlconnector.qa.smppclient.session.MessageListener;
import main.java.mysqlconnector.qa.smppclient.session.Session;
import main.java.mysqlconnector.qa.smppclient.session.impl.BasicSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class EsmeStub implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(EsmeStub.class);

    private int port;

    private String server = "192.168.42.1";

    private Session session = null;
    
    private String system_id;
    
    private String password;
    
    private volatile long sequence;

    private boolean run = true;

    public EsmeStub(){
        port = PropertiesReader.getPort();
        sequence = 0;
        server = PropertiesReader.getServer();
        system_id = PropertiesReader.getSystem_id();
        password = PropertiesReader.getPassword();
    }

    @Override
    public void run() {

        try {
            System.out.println("Esme Stub Configuring");

            session = session(port, new MessageListenerImpl());
            
            BindTransceiver bind = new BindTransceiver();
            bind.setSystemId(system_id);
            bind.setPassword(password);
            bind.setSequenceNumber(getSequenceNumber());
            
            session.open(bind);

            logger.info("Session started, smsc " + server + ":" + port);

            while (run){

                System.out.println("Enter command: submit_sm, unbind, query_sm...");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String command = bufferedReader.readLine();

                if(command.equals("submit_sm")){
                    SubmitSm submitSm = new SubmitSm();
                    submitSm.setSequenceNumber(getSequenceNumber());
                    session.send(submitSm);
                }
                else if(command.equals("mts")){
                    SubmitSm submitSm = new SubmitSm();
                    submitSm.setSequenceNumber(getSequenceNumber());
                    submitSm.setDestinationAddr("000014");
                    submitSm.setSourceAddr("79162316814");

                    System.out.println("Enter short message for submit_sm");
                    String message = bufferedReader.readLine();
                    submitSm.setShortMessage(message.getBytes("UTF-8"));

                    session.send(submitSm);
                }
                else if(command.equals("tele2")){
                    SubmitSm submitSm = new SubmitSm();
                    submitSm.setSequenceNumber(getSequenceNumber());
                    submitSm.setDestinationAddr("000018");
                    submitSm.setSourceAddr("79162316814");

                    System.out.println("Enter short message for submit_sm");
                    String message = bufferedReader.readLine();
                    submitSm.setShortMessage(message.getBytes("UTF-8"));

                    session.send(submitSm);
                }
                else if(command.equals("unbind")){
                    Unbind unbind = new Unbind();
                    unbind.setSequenceNumber(getSequenceNumber());
                    session.send(unbind);
                    run = false;
                }
                else if(command.equals("query_sm")){
                    QuerySm querySm = new QuerySm();
                    querySm.setSequenceNumber(getSequenceNumber());
                    session.send(querySm);
                }
                else {
                    System.out.println("Unknown command, try again");
                }
            }


        }        
        catch (SmppException se){
            logger.error("Smpp application exception", se);
        }
        catch (Exception e){
            logger.error("Error occurred", e);
        }
        finally {
            if(session != null){
                session.close();
            }
            logger.info("Esme was stopped");
        }
    }

    protected synchronized long getSequenceNumber(){
        return ++sequence;
    }

    protected Session session(int port, MessageListener listener) {
        Session session = new BasicSession(new TcpConnection(new InetSocketAddress(server, port)));
        session.setSmscResponseTimeout(500);
        if (listener != null)
            session.setMessageListener(listener);
        return session;
    }

    protected class MessageListenerImpl implements MessageListener {

        private final List<Pdu> pdus = new ArrayList<Pdu>();

        @Override
        public void received(Pdu pdu) {

            pdus.add(pdu);

            logger.info("PDU received: commandId=" + CommandParser.parse(pdu.getCommandId()) +
                    ", commandStatusId=" + pdu.getCommandStatus() +
                    ", seq_num=" + pdu.getSequenceNumber());

            long seqNum = pdu.getSequenceNumber();

            try {
                if(pdu.getCommandId() == CommandId.ENQUIRE_LINK){
                    EnquireLinkResp enquireLinkResp = new EnquireLinkResp();
                    enquireLinkResp.setSequenceNumber(seqNum);
                    session.send(enquireLinkResp);
                }
                else if(pdu.getCommandId() == CommandId.BIND_RECEIVER_RESP){
                    if(pdu.getCommandStatus() == CommandStatus.ESME_ROK){
                        logger.info("Connection success");
                    }
                    else {
                        logger.info("Connection refuse with status: " + pdu.getCommandStatus());
                    }
                }
                else if(pdu.getCommandId() == CommandId.DELIVER_SM){
                    DeliverSmResp deliverSmResp = new DeliverSmResp();
                    deliverSmResp.setSequenceNumber(seqNum);
                    session.send(deliverSmResp);

                    DeliverSm dsm = (DeliverSm) pdu;
                    logger.info("Deliver_SM: source=" + dsm.getDestinationAddr() +
                            ", destination=" + dsm.getDestinationAddr() +
                            ", shortMessage=" + new String(dsm.getShortMessage()));

                }
                else if(pdu.getCommandId() == CommandId.UNBIND){
                    UnbindResp unbindResp = new UnbindResp();
                    unbindResp.setSequenceNumber(seqNum);
                    session.send(unbindResp);
                    run = false;
                }
            }
            catch (SmppException se){
                logger.error("Smpp application exception", se);
            }
            catch (Exception e){
                logger.error("Error occurred", e);
            }


        }

        public void notifyMessageListener() {

        }
    }
}
