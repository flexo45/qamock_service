package org.qamock.script.model;

import org.qamock.script.exception.ScriptExecutionException;
import org.qamock.script.exception.ScriptExtractionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

public class SleepStep implements ScriptStep, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(SleepStep.class);

    private static final long serialVersionUID = 3475642362645766885L;

    public SleepStep(long time){
        this.time = time;
    }

    public void setScriptSuite(ScriptSuite v){scriptSuite = v;}

    @Override
    public String getType() {
        return SleepStep.class.getSimpleName();
    }

    @Override
    public void run() throws ScriptExecutionException {
        try {
            pause();
        }
        catch (InterruptedException e){
            throw new ScriptExecutionException(e);
        }
    }

    @Override
    public void extract() throws ScriptExtractionException {

    }

    @Override
    public Map<String, String> getProperties() {
        return scriptSuite.getProperties();
    }

    private void pause() throws InterruptedException{
        Thread.sleep(time);
        logger.info(scriptSuite.getName() + " slept " + time + " ms");
    }

    private long time;

    private ScriptSuite scriptSuite;
}
