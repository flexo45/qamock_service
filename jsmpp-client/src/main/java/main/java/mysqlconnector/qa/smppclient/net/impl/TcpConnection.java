package main.java.mysqlconnector.qa.smppclient.net.impl;

import main.java.mysqlconnector.qa.smppclient.net.Connection;
import main.java.mysqlconnector.qa.smppclient.pdu.Pdu;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.pdu.PduParser;
import main.java.mysqlconnector.qa.smppclient.pdu.impl.DefaultPduParser;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.locks.ReentrantLock;

public class TcpConnection implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(TcpConnection.class);

    private final SocketAddress socketAddress;
    private final byte[] bytes = new byte[250];
    private PduParser parser = new DefaultPduParser();

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    private ByteBuffer bb;

    private final ReentrantLock readLock = new ReentrantLock();
    private final ReentrantLock writeLock = new ReentrantLock();

    public TcpConnection(SocketAddress socketAddress)
    {
        this.socketAddress = socketAddress;
    }

    @Override
    public void setParser(PduParser parser)
    {
        this.parser = parser;
    }

    @Override
    public void open() throws IOException
    {
        socket = new Socket();
        socket.connect(socketAddress);
        socket.setSoTimeout(0); // block read forever
        in = socket.getInputStream();
        out = socket.getOutputStream();
        bb = new ByteBuffer();
    }

    @Override
    public Pdu read() throws PduException, IOException
    {
        try
        {
            readLock.lock();
            Pdu pdu = tryToReadBuffer(); // there may be two PDU's read previous time, but only one parsed
            while (pdu == null)
            {
                int read = in.read(bytes);
                if (read < 0)
                {
                    throw new IOException("Connection closed by SMSC");
                }
                bb.appendBytes(bytes, read);
                pdu = tryToReadBuffer();
            }
            if (logger.isTraceEnabled())
            {
                logger.trace("<<< {}", pdu.buffer().hexDump());
            }
            return pdu;
        } finally
        {
            readLock.unlock();
        }
    }

    @Override
    public void write(Pdu pdu) throws PduException, IOException
    {
        try
        {
            writeLock.lock();
            out.write(pdu.buffer().array());
            out.flush();
            if (logger.isTraceEnabled())
            {
                logger.trace(">>> {}", pdu.buffer().hexDump());
            }
        } finally
        {
            writeLock.unlock();
        }
    }

    @Override
    public void close()
    {
        try
        {
            writeLock.lock();
            try
            {
                if(socket != null && !socket.isClosed())
                {
                    socket.close();
                }
            } catch (IOException e)
            {
                logger.debug("Connection closing error.", e);
            }
        } finally
        {
            writeLock.unlock();
        }
    }

    private Pdu tryToReadBuffer() throws PduException
    {
        if (bb.length() >= Pdu.HEADER_LENGTH)
        {
            long commandLength = bb.readInt();
            if (bb.length() >= commandLength)
            {
                return parser.parse(new ByteBuffer(bb.removeBytes((int) commandLength)));
            }
        }
        return null;
    }

}
