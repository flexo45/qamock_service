package org.qamock.dynamic.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.qamock.dynamic.domain.DynamicResourceRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class GroovyScriptHandler {

    public static Map<String, Object> executeDispatchScript(String script, Map<String, String> params, DynamicResourceRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        Binding binding = new Binding();
        binding.setVariable("request", request);
        binding.setVariable("params", params);
        binding.setVariable("utils", new ScriptUtils());
        GroovyShell groovyShell = new GroovyShell(binding);
        result.put("response", groovyShell.evaluate(script).toString());
        result.put("params", params);
        return result;
    }

    public static HttpServletResponse executeResponseScript(String script, HttpServletResponse response){
        Binding binding = new Binding();
        binding.setVariable("response", response);
        binding.setVariable("utils", new ScriptUtils());
        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(script);
        return (HttpServletResponse) result;
    }

    public static HttpServletResponse executeResponseScript(String script, Map<String, String> params, HttpServletResponse response){
        Binding binding = new Binding();
        binding.setVariable("response", response);
        binding.setVariable("params", params);
        binding.setVariable("utils", new ScriptUtils());
        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(script);
        return (HttpServletResponse) result;
    }

}
