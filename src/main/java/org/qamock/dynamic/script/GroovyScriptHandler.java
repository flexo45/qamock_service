package org.qamock.dynamic.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.qamock.dynamic.domain.DynamicResourceRequest;
import org.qamock.service.DynamicResourcesService;
import org.qamock.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class GroovyScriptHandler implements ScriptHandler{

    //private DynamicResourcesService resourcesService;

    //private ScriptService scriptService;

    private ScriptUtils scriptUtils;

    @Override
    public void setScriptUtils(ScriptUtils scriptUtils){
        this.scriptUtils = scriptUtils;
    }

    @Override
    public ScriptUtils getScriptUtils(){return this.scriptUtils;}

    @Override
    public Map<String, Object> executeDispatchScript(String script, Map<String, String> params, DynamicResourceRequest request) throws RuntimeException{
        Map<String, Object> result = new HashMap<String, Object>();
        Binding binding = new Binding();
        binding.setVariable("request", request);
        binding.setVariable("params", params);
        binding.setVariable("utils", scriptUtils);
        GroovyShell groovyShell = new GroovyShell(binding);
        result.put("response", groovyShell.evaluate(script));
        result.put("params", params);
        return result;
    }

    @Override
    public void executeResponseScript(String script, HttpServletResponse response) throws RuntimeException{
        Binding binding = new Binding();
        binding.setVariable("response", response);
        binding.setVariable("utils", scriptUtils);
        GroovyShell groovyShell = new GroovyShell(binding);
        groovyShell.evaluate(script);
    }

    @Override
    public void executeResponseScript(String script, Map<String, String> params, HttpServletResponse response) throws RuntimeException{
        Binding binding = new Binding();
        binding.setVariable("response", response);
        binding.setVariable("params", params);
        binding.setVariable("utils", scriptUtils);
        GroovyShell groovyShell = new GroovyShell(binding);
        groovyShell.evaluate(script);
    }

}
