package org.qamock.service;

import org.qamock.dao.ConnectionDao;
import org.qamock.dao.ScriptsDao;
import org.qamock.domain.Connection;
import org.qamock.domain.ScriptSuite;
import org.qamock.script.exception.ScriptInitializationException;
import org.qamock.script.handler.ScriptSuiteProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ScriptServiceImpl implements ScriptService {

    private static final Logger logger = LoggerFactory.getLogger(ScriptServiceImpl.class);

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ScriptsDao scriptsDao;

    @Autowired
    private ConnectionDao connectionDao;

    @Autowired
    private ScriptSuiteProcessor suiteProcessor;

    @Transactional
    @Override
    public void runScriptAsync(String name) {
        ScriptSuite suite;
        try {
            suite = scriptsDao.get(name);
            if(suite != null){
                suiteProcessor.initiateScriptSuite(suite);
                taskExecutor.execute(suiteProcessor);
            }
            else {
                logger.warn("Script not found: " + name);
            }
        }
        catch (ScriptInitializationException sie){
            logger.error("Error on initialize script", sie);
        }
        catch (IOException ioe){
            logger.error("Unknown error",ioe);
        }
    }

    @Transactional
    @Override
    public void runScriptAsync(String name, Map<String, String> params) {
        ScriptSuite suite = scriptsDao.get(name);
        try {
            if(suite != null){
                suiteProcessor.initiateScriptSuite(suite, params);
                taskExecutor.execute(suiteProcessor);
            }
            else {
                logger.warn("Script not found: " + name);
            }
        }
        catch (ScriptInitializationException sie){
            logger.error("Error on initialize script", sie);
        }
        catch (IOException ioe){
            logger.error("Unknown error",ioe);
        }
    }

    @Transactional
    @Override
    public List<ScriptSuite> getSuiteList() {
        return scriptsDao.list();
    }

    @Transactional
    @Override
    public void createSuite(ScriptSuite scriptSuite) {
        scriptsDao.add(scriptSuite);
    }

    @Transactional
    @Override
    public void updateSuite(ScriptSuite scriptSuite) {
        scriptsDao.update(scriptSuite);
    }

    @Transactional
    @Override
    public void deleteSuite(ScriptSuite scriptSuite) {
        scriptsDao.delete(scriptSuite);
    }

    @Transactional
    @Override
    public ScriptSuite getSuite(long id) {
        return scriptsDao.get(id);
    }

    @Transactional
    @Override
    public Connection getConnection(String name){
        return connectionDao.get(name);
    }

    @Transactional
    @Override
    public Connection getConnection(long id){
        return connectionDao.get(id);
    }

    @Override
    @Deprecated
    public void reloadScript(String name) {

    }

    @Override
    @Deprecated
    public void reloadScripts() {

    }

    /*
    @Deprecated
    private class RunScriptTask implements Runnable{

        ScriptSuite scriptSuite;

        public RunScriptTask(ScriptSuite scriptSuite){
            this.scriptSuite = scriptSuite;
        }

        @Override
        public void run() {

        }
    }*/
}
