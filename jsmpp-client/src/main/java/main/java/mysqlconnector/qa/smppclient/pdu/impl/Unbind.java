package main.java.mysqlconnector.qa.smppclient.pdu.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;

public class Unbind extends AbstractPdu {

    public Unbind()
    {
        super(CommandId.UNBIND);
    }

    Unbind(ByteBuffer bb) throws PduException
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
        return "Unbind{" + super.toString() + "}";
    }

}
