package main.java.mysqlconnector.qa.smppclient.pdu.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;
import main.java.mysqlconnector.qa.smppclient.util.TerminatingNullNotFoundException;

public class BindReceiverResp extends AbstractPdu {

    /**
     * SMSC identifier. Identifies the SMSC to the ESME.
     */
    private String systemId;

    public BindReceiverResp() {
        super(CommandId.BIND_RECEIVER_RESP);
    }

    BindReceiverResp(ByteBuffer bb) throws PduException {
        super(bb);
        try {
            systemId = bb.removeCString();
        } catch (TerminatingNullNotFoundException e) {
            throw new PduException("System id parsing failed.", e);
        }
    }

    @Override
    protected ByteBuffer body() {
        ByteBuffer bb = new ByteBuffer();
        bb.appendCString(systemId);
        return bb;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String toString()
    {
        return "BindReceiverResp{" + super.toString() + "}";
    }

}
