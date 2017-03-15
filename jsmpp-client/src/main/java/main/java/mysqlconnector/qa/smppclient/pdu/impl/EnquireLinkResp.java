package main.java.mysqlconnector.qa.smppclient.pdu.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;

public class EnquireLinkResp extends AbstractPdu {

    public EnquireLinkResp()
    {
        super(CommandId.ENQUIRE_LINK_RESP);
    }

    protected EnquireLinkResp(ByteBuffer bb) throws PduException
    {
        super(bb);
    }

    @Override
    protected ByteBuffer body()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "EnquireLinkResp{" + super.toString() + "}";
    }

}
