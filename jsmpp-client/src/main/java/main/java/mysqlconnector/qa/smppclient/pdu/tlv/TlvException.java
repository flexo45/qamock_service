package main.java.mysqlconnector.qa.smppclient.pdu.tlv;

import main.java.mysqlconnector.qa.smppclient.SmppException;

public class TlvException extends SmppException {

    public TlvException() {
        super();
    }

    public TlvException(String message) {
        super(message);
    }

    public TlvException(String message, Throwable cause) {
        super(message, cause);
    }

    public TlvException(Throwable cause) {
        super(cause);
    }

}
