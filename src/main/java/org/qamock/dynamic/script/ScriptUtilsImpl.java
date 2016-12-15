package org.qamock.dynamic.script;

import org.qamock.service.ScriptService;
import org.qamock.service.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ScriptUtilsImpl implements ScriptUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScriptUtilsImpl.class);

    public ScriptUtilsImpl(ScriptService scriptService, SequenceService sequenceService){
        this.sequenceService = sequenceService;
        this.scriptService = scriptService;
    }

    private SequenceService sequenceService;

    private ScriptService scriptService;

    @Override
    public void asyncScript(String name) {
        scriptService.runScriptAsync(name);
    }

    @Override
    public void asyncScript(String name, Map<String, String> params) {
        scriptService.runScriptAsync(name, params);
    }

    @Override
    public void log(String text) {
        logger.info("[Groovy.log]: " + text);
    }

    @Override
    public long seqNext(String name) {
        return sequenceService.nextSequenceNumber(name);
    }

    @Override
    public long seqCurrent(String name) {
        return sequenceService.currentSequenceNumber(name);
    }
}
