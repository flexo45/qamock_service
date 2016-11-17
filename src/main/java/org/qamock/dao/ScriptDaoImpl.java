package org.qamock.dao;

import org.qamock.config.XmlFileScriptsDataSource;
import org.qamock.script.model.ScriptSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class ScriptDaoImpl implements ScriptsDao {

    private static final Logger logger = LoggerFactory.getLogger(ScriptDaoImpl.class);

    @Autowired
    XmlFileScriptsDataSource scriptDataSource;

    @Override
    public Map<String, ScriptSuite> getSuites() throws Exception{
        Map<String, ScriptSuite> result = new HashMap<String, ScriptSuite>();
        String[] scriptNameList = scriptDataSource.listScripts();
        for(int i = 0; i < scriptNameList.length; i++){
            try {
                ScriptSuite suite = scriptDataSource.readSuite(scriptNameList[i]);
                logger.info("Script '" + suite.getName() + "' readed");
                result.put(suite.getName(), suite);
            }
            catch (Exception e){
                logger.error("Error on read script: " + scriptNameList[i], e);
            }
        }

        return result;
    }

    @Override
    public ScriptSuite getSuite(String name) throws Exception {
        return scriptDataSource.readSuite(name);
    }
}
