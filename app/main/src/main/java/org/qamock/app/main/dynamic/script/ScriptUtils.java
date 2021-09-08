package org.qamock.app.main.dynamic.script;

import groovy.util.slurpersupport.GPathResult;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

public interface ScriptUtils {

    void asyncScript(String name);

    void asyncScript(String name, Map<String, String> params);

    void log(String text);

    long seqNext(String name);

    long seqCurrent(String name);

    GPathResult xml(String rawTest) throws ParserConfigurationException, SAXException, IOException;

    String fileBase64(String fileName) throws IOException;

    String fileString(String fileName) throws IOException;

    byte[] fileBytes(String fileName) throws IOException;
}
