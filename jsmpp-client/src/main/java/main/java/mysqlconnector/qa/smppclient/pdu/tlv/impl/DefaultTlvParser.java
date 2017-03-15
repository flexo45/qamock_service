package main.java.mysqlconnector.qa.smppclient.pdu.tlv.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.tlv.Tlv;
import main.java.mysqlconnector.qa.smppclient.pdu.tlv.TlvException;
import main.java.mysqlconnector.qa.smppclient.pdu.tlv.TlvParser;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;

import java.util.HashMap;
import java.util.Map;

public class DefaultTlvParser implements TlvParser {

    @Override
    public Map<Integer, Tlv> parse(ByteBuffer bb) throws TlvException {
        final byte[] original = bb.array();
        Map<Integer, Tlv> tlvs = null;
        if (bb.length() >= 4)
            tlvs = new HashMap<Integer, Tlv>();
        try {
            while (bb.length() > 0) {
                int tag = bb.removeShort();
                int length = bb.removeShort();
                byte[] value = bb.removeBytes(length);
                Tlv tlv = new TlvImpl(tag);
                tlv.setValue(value);
                tlvs.put(tag, tlv);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new TlvException("Malformed TLV part: " + new ByteBuffer(original).hexDump() + ".", e);
        }
        return tlvs;
    }

}
