package main.java.mysqlconnector.qa.smppclient.pdu.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;
import main.java.mysqlconnector.qa.smppclient.util.TerminatingNullNotFoundException;

public class DeliverSmResp extends AbstractPdu {

    public DeliverSmResp() {
        super(CommandId.DELIVER_SM_RESP);
    }

    public DeliverSmResp(ByteBuffer bb) throws PduException {
        super(bb);
        if (bb.length() > 0){
            try {
                bb.removeCString();
            } catch (TerminatingNullNotFoundException e) {
                throw new PduException("Message id parsing failed.", e);
            }
        }
    }

    @Override
    protected ByteBuffer body() {
        return new ByteBuffer().appendByte(0);
    }

    @Override
    public String toString()
    {
        return "DeliverSmResp{" + super.toString() + "}";
    }

}
