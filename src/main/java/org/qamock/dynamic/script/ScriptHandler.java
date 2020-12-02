package org.qamock.dynamic.script;


import org.qamock.dynamic.domain.DynamicResourceRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ScriptHandler {

    Map<String, Object> executeDispatchScript(String script, Map<String, String> params, DynamicResourceRequest request) throws RuntimeException;

    void executeResponseScript(String script, HttpServletResponse response) throws RuntimeException;

    void executeResponseScript(String script, Map<String, String> params, HttpServletResponse response) throws RuntimeException;
}
