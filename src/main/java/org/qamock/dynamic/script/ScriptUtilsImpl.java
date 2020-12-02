package org.qamock.dynamic.script;

import org.qamock.script.ScriptExecutorUtilImpl;
import org.qamock.script.SequenceUtilImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ScriptUtilsImpl implements ScriptUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScriptUtilsImpl.class);

    private SequenceUtilImpl sequenceUtil;

    private ScriptExecutorUtilImpl scriptExecutorUtils;



    public ScriptExecutorUtilImpl getScriptExecutorUtils(){return this.scriptExecutorUtils;}

    public SequenceUtilImpl getSequenceUtil(){return this.sequenceUtil;}

    public void setScriptExecutorUtils(ScriptExecutorUtilImpl scriptExecutorUtils){this.scriptExecutorUtils = scriptExecutorUtils;}

    public void setSequenceUtil(SequenceUtilImpl sequenceUtil){this.sequenceUtil = sequenceUtil;}



    @Override
    public void asyncScript(String name) {
        scriptExecutorUtils.runScriptAsync(name);
    }

    @Override
    public void asyncScript(String name, Map<String, String> params) {
        scriptExecutorUtils.runScriptAsync(name, params);
    }

    @Override
    public void log(String text) {
        logger.info("[Groovy.log]: " + text);
    }

    @Override
    public long seqNext(String name) {
        return sequenceUtil.nextSequenceNumber(name);
    }

    @Override
    public long seqCurrent(String name) {
        return sequenceUtil.currentSequenceNumber(name);
    }
}
