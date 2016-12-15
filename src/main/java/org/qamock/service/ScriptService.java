package org.qamock.service;

import org.qamock.domain.Connection;
import org.qamock.domain.ScriptSuite;

import java.util.List;
import java.util.Map;

public interface ScriptService {

    void runScriptAsync(String name);

    void runScriptAsync(String name, Map<String, String> params);

    List<ScriptSuite> getSuiteList();

    void createSuite(ScriptSuite scriptSuite);

    void updateSuite(ScriptSuite scriptSuite);

    void deleteSuite(ScriptSuite scriptSuite);

    ScriptSuite getSuite(long id);

    Connection getConnection(String id);

    Connection getConnection(long id);

    @Deprecated
    void reloadScript(String name);

    @Deprecated
    void reloadScripts();

}
