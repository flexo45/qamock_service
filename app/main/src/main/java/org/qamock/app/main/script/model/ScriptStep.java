package org.qamock.app.main.script.model;

import org.qamock.app.main.script.exception.ScriptExecutionException;
import org.qamock.app.main.script.exception.ScriptExtractionException;

import java.util.Map;

public interface ScriptStep {

    String getType();

    void run() throws ScriptExecutionException, ScriptExecutionException;

    void extract() throws ScriptExtractionException;

    Map<String, String> getProperties();

}
