package org.qamock.app.main.script.exception;

public class ScriptInitializationException extends Exception {

    public ScriptInitializationException(){
        super();
    }

    public ScriptInitializationException(String text){
        super(text);
    }

    public ScriptInitializationException(Throwable cause){
        super(cause);
    }

    public ScriptInitializationException(String text, Throwable cause){
        super(text, cause);
    }

}
