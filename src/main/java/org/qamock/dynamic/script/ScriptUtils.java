package org.qamock.dynamic.script;

import java.util.Map;

public interface ScriptUtils {

    void asyncScript(String name);

    void asyncScript(String name, Map<String, String> params);

    void log(String text);

    long seqNext(String name);

    long seqCurrent(String name);

}
