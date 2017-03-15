package main.java.mysqlconnector.qa.smppclient.pdu;

import main.java.mysqlconnector.qa.smppclient.SmppException;

public class PduException extends SmppException {

    public PduException() {
        super();
    }

    public PduException(String message) {
        super(message);
    }

    public PduException(String message, Throwable cause) {
        super(message, cause);
    }

    public PduException(Throwable cause) {
        super(cause);
    }

}
