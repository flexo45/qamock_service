package org.qamock.dao;

import org.qamock.domain.Sequence;
import org.qamock.domain.ScriptSuite;

import java.util.List;
import java.util.Map;

public interface ScriptsDao {

    List<ScriptSuite> list();

    ScriptSuite get(String name);

    ScriptSuite get(long id);

    void add(ScriptSuite scriptSuite);

    void update(ScriptSuite scriptSuite);

    void delete(ScriptSuite scriptSuite);

}
