package main.java.mysqlconnector.qa.smppclient.pdu;

import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;

public interface PduParser {

    /**
     * Parse PDU.
     *
     * @param bb one pdu bytes
     * @return Pdu
     * @throws PduException pdu parsing failed
     */
    Pdu parse(ByteBuffer bb) throws PduException;

}
