package main.java.mysqlconnector.qa.smppclient.pdu.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;

public class EnquireLink extends AbstractPdu {

    public EnquireLink()
    {
        super(CommandId.ENQUIRE_LINK);
    }

    protected EnquireLink(ByteBuffer bb) throws PduException
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
        return "EnquireLink{" + super.toString() + "}";
    }

}
