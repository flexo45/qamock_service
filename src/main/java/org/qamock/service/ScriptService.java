package org.qamock.service;

import org.qamock.script.model.ScriptSuite;

import java.util.Map;

public interface ScriptService {

    void runScriptAsync(String name);

    void runScriptAsync(String name, Map<String, String> params);

    Map<String, ScriptSuite> getSuitesMap();

    void reloadScript(String name);

    void reloadScripts();

}
