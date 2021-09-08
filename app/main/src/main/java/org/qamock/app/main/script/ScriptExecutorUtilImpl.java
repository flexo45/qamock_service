package org.qamock.app.main.script;

import org.qamock.app.main.dao.ScriptsDao;
import org.qamock.app.main.script.exception.ScriptInitializationException;
import org.qamock.app.main.script.handler.ScriptSuiteProcessor;
import org.qamock.app.main.domain.ScriptSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Component
public class ScriptExecutorUtilImpl {

    private static final Logger logger = LoggerFactory.getLogger(ScriptExecutorUtilImpl.class);

    public ScriptExecutorUtilImpl(@Qualifier("taskExecutor") TaskExecutor taskExecutor,
                           ScriptsDao scriptsDao,
                           ScriptSuiteProcessor suiteProcessor) {
        this.taskExecutor = taskExecutor;
        this.scriptsDao = scriptsDao;
        this.suiteProcessor = suiteProcessor;
    }
    private final TaskExecutor taskExecutor;
    private final ScriptsDao scriptsDao;
    private final ScriptSuiteProcessor suiteProcessor;

    @Transactional
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
}
