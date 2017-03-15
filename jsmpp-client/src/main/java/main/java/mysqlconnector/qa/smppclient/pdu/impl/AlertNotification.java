package main.java.mysqlconnector.qa.smppclient.pdu.impl;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;
import main.java.mysqlconnector.qa.smppclient.pdu.PduException;
import main.java.mysqlconnector.qa.smppclient.util.ByteBuffer;
import main.java.mysqlconnector.qa.smppclient.util.TerminatingNullNotFoundException;

public class AlertNotification extends AbstractPdu {

    /**
     * Type of number for the MS which has become available. If not known, set
     * to NULL.
     */
    private int sourceAddrTon;
    /**
     * Numbering Plan Indicator for the MS which has become available. If not
     * known, set to NULL.
     */
    private int sourceAddrNpi;
    /**
     * Address of MS which has become available.
     */
    private String sourceAddr;
    /**
     * Type of number for destination address which requested an alert on a
     * particular MS becoming available. If not known, set to NULL.
     */
    private int esmeAddrTon;
    /**
     * Numbering Plan Indicator for ESME which requested an alert on a
     * particular MS becoming available. If not known, set to NULL.
     */
    private int esmeAddrNpi;
    /**
     * Address of ESME which requested an alert on a particular MS becoming
     * available.
     */
    private String esmeAddr;

    public AlertNotification() {
        super(CommandId.ALERT_NOTIFICATION);
    }

    AlertNotification(ByteBuffer bb) throws PduException {
        super(bb);
        sourceAddrTon = bb.removeByte();
        sourceAddrNpi = bb.removeByte();
        try {
            sourceAddr = bb.removeCString();
        } catch (TerminatingNullNotFoundException e) {
            throw new PduException("Source address parsing failed", e);
        }
        esmeAddrTon = bb.removeByte();
        esmeAddrNpi = bb.removeByte();
        try {
            esmeAddr = bb.removeCString();
        } catch (TerminatingNullNotFoundException e) {
            throw new PduException("Esme address parsing failed", e);
        }
    }

    @Override
    protected ByteBuffer body() {
        ByteBuffer bb = new ByteBuffer();
        bb.appendByte(sourceAddrTon);
        bb.appendByte(sourceAddrNpi);
        bb.appendCString(sourceAddr);
        bb.appendByte(esmeAddrTon);
        bb.appendByte(esmeAddrNpi);
        bb.appendCString(esmeAddr);
        return bb;
    }

    public int getSourceAddrTon() {
        return sourceAddrTon;
    }

    public void setSourceAddrTon(int sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }

    public int getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    public void setSourceAddrNpi(int sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }

    public String getSourceAddr() {
        return sourceAddr;
    }

    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    public int getEsmeAddrTon() {
        return esmeAddrTon;
    }

    public void setEsmeAddrTon(int esmeAddrTon) {
        this.esmeAddrTon = esmeAddrTon;
    }

    public int getEsmeAddrNpi() {
        return esmeAddrNpi;
    }

    public void setEsmeAddrNpi(int esmeAddrNpi) {
        this.esmeAddrNpi = esmeAddrNpi;
    }

    public String getEsmeAddr() {
        return esmeAddr;
    }

    public void setEsmeAddr(String esmeAddr) {
        this.esmeAddr = esmeAddr;
    }

    public String toString()
    {
        return "AlertNotification{" + super.toString() + "}";
    }

}
