package org.qamock.service;

import org.qamock.dao.ScriptsDao;
import org.qamock.script.model.ScriptSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScriptServiceImpl implements ScriptService {

    private static final Logger logger = LoggerFactory.getLogger(ScriptServiceImpl.class);

    @Autowired
    TaskExecutor taskExecutor;

    @Autowired
    ScriptsDao scriptsDao;

    @Override
    public void runScriptAsync(String name) {
        try {
            ScriptSuite suite = scriptsDao.getSuite(name);
            //ScriptSuite suite = suitesMap.get(name).copy(); //TODO cache
            if(suite != null){
                taskExecutor.execute(new RunScriptTask(suite));
            }
            else {
                logger.warn("Script not found: " + name);
            }
        }
        catch (Exception e){
            logger.error("Error on run script: " + e, e);
        }
    }

    @Override
    public void runScriptAsync(String name, Map<String, String> params) {
        try {
            ScriptSuite suite = scriptsDao.getSuite(name);
            //ScriptSuite suite = suitesMap.get(name).copy(); //TODO cache
            if(suite != null){
                for(Map.Entry<String, String> p : params.entrySet()){
                    suite.getProperties().put(p.getKey(), p.getValue());
                }
                taskExecutor.execute(new RunScriptTask(suite));
            }
            else {
                logger.warn("Script not found: " + name);
            }
        }
        catch (Exception e){
            logger.error("Error on run script: " + e, e);
        }
    }

    @Override
    public Map<String, ScriptSuite> getSuitesMap() {
        try {
            return scriptsDao.getSuites();
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    public void reloadScript(String name) {

    }

    @Override
    public void reloadScripts() {

    }

    private class RunScriptTask implements Runnable{

        ScriptSuite scriptSuite;

        public RunScriptTask(ScriptSuite scriptSuite){
            this.scriptSuite = scriptSuite;
        }

        @Override
        public void run() {

        }
    }
}
