package main.java.mysqlconnector.qa.smppclient;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.impl.*;
import main.java.mysqlconnector.qa.smppclient.session.Session;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ComplexSmscStub implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ComplexSmscStub.class);

    public List<byte[]> input = new ArrayList<byte[]>();

    private final int port;
    private volatile ServerSocket server;
    private volatile OutputStream out;
    private volatile long sequence;
    private volatile EnquireLinkWorker enquireLinkWorker;

    private Thread listener;
    private Socket client;
    private volatile boolean run = true;

    public ComplexSmscStub(int port) {
        this.port = port;
    }

    public void start() throws IOException, InterruptedException {
        listener = new Thread(this);
        listener.start();
        synchronized (this) {
            wait();
        }
        logger.info("SMSC started on port=" + port);
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            synchronized (this) {
                notify();
            }
            client = server.accept();
            client.setSoTimeout(0);
            InputStream in = client.getInputStream();
            out = client.getOutputStream();
            byte[] bytes = new byte[1024];
            do {
                int read = in.read(bytes);
                if (read < 0)
                    break;
                byte[] pdu = new byte[read];
                System.arraycopy(bytes, 0, pdu, 0, read);
                input.add(pdu);
                ByteBuffer bb = new ByteBuffer().appendBytes(bytes, read);
                long commandId = bb.readInt(4);
                if (commandId < CommandId.GENERIC_NACK) {
                    long seqNum = bb.readInt(12);
                    logger.info("PDU received, commandId=" + CommandParser.parse(commandId) + ", seq_num=" + seqNum);
                    if (CommandId.BIND_TRANSCEIVER == commandId) {
                        BindTransceiverResp bindResp = new BindTransceiverResp();
                        bindResp.setSystemId(Long.toString(System.currentTimeMillis()));
                        bindResp.setSequenceNumber(seqNum);
                        out.write(bindResp.buffer().array());
                        logger.info("send BindTransceiverResp, seqNum=" + seqNum);
                    } else if (CommandId.ENQUIRE_LINK == commandId) {
                        EnquireLinkResp enquireLinkResp = new EnquireLinkResp();
                        enquireLinkResp.setSequenceNumber(seqNum);
                        out.write(enquireLinkResp.buffer().array());
                        logger.info("send EnquireLinkResp, seqNum=" + seqNum);
                    } else if (CommandId.SUBMIT_SM == commandId) {
                        SubmitSmResp submitSmResp = new SubmitSmResp();
                        submitSmResp.setMessageId(Long.toString(System.currentTimeMillis()));
                        submitSmResp.setSequenceNumber(seqNum);
                        out.write(submitSmResp.buffer().array());
                        logger.info("send SubmitSmResp, seqNum=" + seqNum);
                    } else if (CommandId.UNBIND == commandId) {
                        UnbindResp unbindResp = new UnbindResp();
                        unbindResp.setSequenceNumber(seqNum);
                        out.write(unbindResp.buffer().array());
                        logger.info("send UnbindResp, seqNum=" + seqNum);
                    }
                }
            } while (run);
            client.close();
        } catch (Exception e) {
            if (run) {
                logger.error("SMSC execution failed.", e);
            }
        } finally {
            stop();
            logger.info("SMSC was stopped");

        }

    }

    public void write(byte[] bytes) throws IOException, InterruptedException {
        while (out == null)
            Thread.sleep(10);
        out.write(bytes);
    }

    protected synchronized long getSequenceNumber(){
        return ++sequence;
    }

    public synchronized void stop() {
        if (run) {
            run = false;
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ignore) {
                    // omit it
                }
            }
            listener.interrupt();
        }
    }

    private class EnquireLinkWorker implements Runnable{

        private Thread thread;

        private Session session;

        private boolean run = true;

        public EnquireLinkWorker(Session session){
            this.session = session;
        }

        @Override
        public void run() {
            logger.info("EnquireLinkWorker started");
            while (run){
                try {
                    EnquireLink enquireLink = new EnquireLink();
                    enquireLink.setSequenceNumber(getSequenceNumber());
                    session.send(enquireLink);
                    logger.info("send EnquireLink, seqNum=" + enquireLink.getSequenceNumber());
                    Thread.sleep(20000);
                }
                catch (SmppException se){
                    logger.error("EnquireLinkWorker.Smpp application exception", se);
                }
                catch (Exception e){
                    logger.error("EnquireLinkWorker.Error occurred", e);
                }
            }
            logger.info("EnquireLinkWorker was stopped");
        }

        public void start(){
            if(thread == null){
                thread = new Thread(this);
            }
            thread.start();
        }

        public void stop(){
            run = false;
        }
    }

}
