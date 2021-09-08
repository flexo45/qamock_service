package org.qamock.app.main.service;

import org.qamock.app.main.domain.Connection;
import org.qamock.app.main.domain.ScriptSuite;
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
