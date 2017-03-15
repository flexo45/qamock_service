package main.java.mysqlconnector.qa.smppclient.pdu.tlv.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.tlv.Tlv;
import main.java.mysqlconnector.qa.smppclient.pdu.tlv.TlvException;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;

public class TlvImpl implements Tlv {

    private final int tag;

    private byte[] value;

    public TlvImpl(int tag) {
        this.tag = tag;
    }

    @Override
    public ByteBuffer buffer() throws TlvException {
        ByteBuffer bb = new ByteBuffer();
        bb.appendShort(tag);
        int length = value != null ? value.length : 0;
        bb.appendShort(length);
        if (length > 0)
            bb.appendBytes(value);
        return bb;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public void setValue(byte[] valueBytes) {
        this.value = valueBytes;
    }

}
