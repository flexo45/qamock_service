package org.qamock.app.main.dynamic.script;


import org.qamock.app.main.dynamic.domain.DynamicResourceRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ScriptHandler {

    Map<String, Object> executeDispatchScript(String script, Map<String, String> params, DynamicResourceRequest request) throws RuntimeException;

    void executeResponseScript(String script, HttpServletResponse response) throws RuntimeException;

    void executeResponseScript(String script, Map<String, String> params, HttpServletResponse response) throws RuntimeException;
}
