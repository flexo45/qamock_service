package org.qamock.script;

import org.qamock.dao.ScriptsDao;
import org.qamock.domain.ScriptSuite;
import org.qamock.dynamic.context.TestContextService;
import org.qamock.script.exception.ScriptInitializationException;
import org.qamock.script.handler.ScriptSuiteProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Component
public class ScriptExecutorUtilImpl {

    private static final Logger logger = LoggerFactory.getLogger(ScriptExecutorUtilImpl.class);

    @Qualifier("taskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ScriptsDao scriptsDao;

    @Autowired
    private ScriptSuiteProcessor suiteProcessor;

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
