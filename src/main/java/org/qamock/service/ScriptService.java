package org.qamock.service;

import org.qamock.domain.Connection;
import org.qamock.domain.ScriptSuite;
import java.util.List;

public interface ScriptService {

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
