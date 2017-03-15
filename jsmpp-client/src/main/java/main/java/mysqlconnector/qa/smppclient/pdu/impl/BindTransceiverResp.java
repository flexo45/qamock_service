package main.java.mysqlconnector.qa.smppclient.pdu.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;
import main.java.mysqlconnector.qa.smppclient.util.TerminatingNullNotFoundException;

public class BindTransceiverResp extends AbstractPdu {

    /**
     * SMSC identifier. Identifies the SMSC to the ESME.
     */
    private String systemId;

    public BindTransceiverResp() {
        super(CommandId.BIND_TRANSCEIVER_RESP);
    }

    BindTransceiverResp(ByteBuffer bb) throws PduException {
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

    @Override
    public String toString()
    {
        return "BindTransceiverResp{systemId=" + this.systemId + ", " + super.toString() + "}";
    }

}
