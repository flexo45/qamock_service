package main.java.mysqlconnector.qa.smppclient.pdu.tlv;

import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;

import java.util.Map;

public interface TlvParser {

    /**
     * Parse TLV's from bytes.
     *
     * @param bb byte buffer
     * @return map parameter tag to tlv, may be null
     * @throws TlvException tlv parsing error
     */
    Map<Integer, Tlv> parse(ByteBuffer bb) throws TlvException;

}
