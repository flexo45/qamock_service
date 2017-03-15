package main.java.mysqlconnector.qa.smppclient.util;

import main.java.mysqlconnector.qa.smppclient.SmppException;

public class TerminatingNullNotFoundException extends SmppException {

    public TerminatingNullNotFoundException() {
        super("C-Octet String terminating zero not found.");
    }
}
