package org.qamock.script.model;

import org.qamock.script.exception.ScriptExecutionException;
import org.qamock.script.exception.ScriptExtractionException;

import java.util.Map;

public interface ScriptStep {

    String getType();

    void run() throws ScriptExecutionException, ScriptExecutionException;

    void extract() throws ScriptExtractionException;

    Map<String, String> getProperties();

}
