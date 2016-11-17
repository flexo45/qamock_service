package org.qamock.script.data;

import org.qamock.config.XmlFileScriptsDataSource;
import org.qamock.script.model.ScriptSuite;

public class ScriptsDaoWrapper implements XmlFileScriptsDataSource{

    @Override
    public void setScriptLocation(String location) {
        ScriptsDaoGroovyImpl.setScriptLocation(location);
    }

    @Override
    public ScriptSuite readSuite(String name) throws Exception {
        return ScriptsDaoGroovyImpl.readSuite(name);
    }

    @Override
    public String[] listScripts() throws Exception {
        return ScriptsDaoGroovyImpl.listScripts();
    }
}
