package org.qamock.app.main.script.exception;

public class ScriptExtractionException extends Exception {

    public ScriptExtractionException() {
        super();
    }

    public ScriptExtractionException(Throwable e) {
        super(e);
    }

    public ScriptExtractionException(String text, Throwable e){
        super(text, e);
    }

}
