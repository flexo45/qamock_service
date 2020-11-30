package org.qamock.script.handler;

import org.qamock.domain.ScriptSuite;
import org.qamock.script.exception.ScriptInitializationException;
import java.io.IOException;
import java.util.Map;

public interface ScriptSuiteProcessor extends Runnable {

    void initiateScriptSuite(ScriptSuite scriptSuite, Map<String, String> params) throws IOException, ScriptInitializationException;

    void initiateScriptSuite(ScriptSuite scriptSuite) throws IOException, ScriptInitializationException;

    Map<String, String> getProperties();

}
