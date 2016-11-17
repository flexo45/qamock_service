package org.qamock.config;

import org.qamock.script.model.ScriptSuite;

public interface XmlFileScriptsDataSource {

    void setScriptLocation(String location);

    ScriptSuite readSuite(String name) throws Exception;

    String[] listScripts() throws Exception;

}
