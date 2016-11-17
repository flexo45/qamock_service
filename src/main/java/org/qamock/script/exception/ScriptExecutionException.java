package org.qamock.script.exception;

public class ScriptExecutionException extends Exception {
    public ScriptExecutionException() {
        super();
    }

    public ScriptExecutionException(Throwable e) {
        super(e);
    }

    public ScriptExecutionException(String text, Throwable e){
        super(text, e);
    }
}
