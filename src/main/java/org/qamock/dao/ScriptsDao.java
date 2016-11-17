package org.qamock.dao;

import org.qamock.domain.Sequence;
import org.qamock.script.model.ScriptSuite;

import java.util.Map;

public interface ScriptsDao {

    Map<String, ScriptSuite> getSuites() throws Exception;

    ScriptSuite getSuite(String name) throws Exception;

}
