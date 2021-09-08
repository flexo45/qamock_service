package org.qamock.app.main.dao;

import org.qamock.app.main.domain.ScriptSuite;

import java.util.List;

public interface ScriptsDao {

    List<ScriptSuite> list();

    ScriptSuite get(String name);

    ScriptSuite get(long id);

    void add(ScriptSuite scriptSuite);

    void update(ScriptSuite scriptSuite);

    void delete(ScriptSuite scriptSuite);

}
