package main.java.mysqlconnector.qa.smppclient.pdu.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;

public class GenericNack extends AbstractPdu {

    public GenericNack()
    {
        super(CommandId.GENERIC_NACK);
    }

    GenericNack(ByteBuffer bb) throws PduException
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
        return "GenericNack{" + super.toString() + "}";
    }

}
