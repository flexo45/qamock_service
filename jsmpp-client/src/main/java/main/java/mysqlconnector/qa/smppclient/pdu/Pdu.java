package main.java.mysqlconnector.qa.smppclient.pdu;

import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;

public interface Pdu {

    /**
     * Header length.
     */
    public static final int HEADER_LENGTH = 16;

    /**
     * Calculate and return PDU bytes.
     *
     * @return  pdu bytes
     * @throws PduException pdu contains wrong values
     */
    public ByteBuffer buffer() throws PduException;

    public long getCommandId();

    public long getCommandStatus();

    public long getSequenceNumber();

    public void setCommandStatus(long commandStatus);

    public void setSequenceNumber(long sequenceNumber);

}
