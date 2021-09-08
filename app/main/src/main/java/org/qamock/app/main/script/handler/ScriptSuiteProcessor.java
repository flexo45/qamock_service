package org.qamock.app.main.script.handler;

import org.qamock.app.main.script.exception.ScriptInitializationException;
import org.qamock.app.main.domain.ScriptSuite;

import java.io.IOException;
import java.util.Map;

public interface ScriptSuiteProcessor extends Runnable {

    void initiateScriptSuite(ScriptSuite scriptSuite, Map<String, String> params) throws IOException, ScriptInitializationException;

    void initiateScriptSuite(ScriptSuite scriptSuite) throws IOException, ScriptInitializationException;

    Map<String, String> getProperties();

}
