package org.qamock.app.main.dynamic.script;

import groovy.util.XmlSlurper;
import groovy.util.slurpersupport.GPathResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.qamock.app.main.script.SequenceUtilImpl;
import org.qamock.app.main.script.ScriptExecutorUtilImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class ScriptUtilsImpl implements ScriptUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScriptUtilsImpl.class);

    public ScriptUtilsImpl(ScriptExecutorUtilImpl scriptExecutorUtils,
                           SequenceUtilImpl sequenceUtil) {
        this.scriptExecutorUtils = scriptExecutorUtils;
        this.sequenceUtil = sequenceUtil;
    }

    private final SequenceUtilImpl sequenceUtil;
    private final ScriptExecutorUtilImpl scriptExecutorUtils;

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

    @Override
    public GPathResult xml(String rawTest) throws ParserConfigurationException, SAXException, IOException {
        return new XmlSlurper().parseText(rawTest);
    }

    @Override
    public byte[] fileBytes(String fileName) throws IOException {
        File file = new File("resources/files/" + fileName);
        return Base64.encodeBase64(FileUtils.readFileToByteArray(file));
    }

    @Override
    public String fileBase64(String fileName) throws IOException {
        return new String(fileBytes(fileName), StandardCharsets.US_ASCII);
    }

    @Override
    public String fileString(String fileName) throws IOException {
        File file = new File("resources/files/" + fileName);
        return new String(FileUtils.readFileToByteArray(file), StandardCharsets.UTF_8);
    }
}
