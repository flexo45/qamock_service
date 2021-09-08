package org.qamock.app.main.dynamic.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.qamock.app.main.dynamic.context.ContextUtils;
import org.qamock.app.main.dynamic.datagen.DataGenerator;
import org.qamock.app.main.dynamic.domain.DynamicResourceRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class GroovyScriptHandler implements ScriptHandler {

    public GroovyScriptHandler(ScriptUtils scriptUtils,
                               ContextUtils contextUtils,
                               DataGenerator dataGenerator) {
        this.scriptUtils = scriptUtils;
        this.contextUtils = contextUtils;
        this.dataGenerator = dataGenerator;
    }

    private final ScriptUtils scriptUtils;
    private final ContextUtils contextUtils;
    private final DataGenerator dataGenerator;

    @Override
    public Map<String, Object> executeDispatchScript(String script, Map<String, String> params, DynamicResourceRequest request) throws RuntimeException{
        Map<String, Object> result = new HashMap<String, Object>();
        Binding binding = new Binding();
        binding.setVariable("request", request);
        binding.setVariable("params", params);
        binding.setVariable("utils", scriptUtils);
        binding.setVariable("context", contextUtils);
        binding.setVariable("gen", dataGenerator);
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
        binding.setVariable("context", contextUtils);
        binding.setVariable("gen", dataGenerator);
        GroovyShell groovyShell = new GroovyShell(binding);
        groovyShell.evaluate(script);
    }

    @Override
    public void executeResponseScript(String script, Map<String, String> params, HttpServletResponse response) throws RuntimeException{
        Binding binding = new Binding();
        binding.setVariable("response", response);
        binding.setVariable("params", params);
        binding.setVariable("utils", scriptUtils);
        binding.setVariable("context", contextUtils);
        binding.setVariable("gen", dataGenerator);
        GroovyShell groovyShell = new GroovyShell(binding);
        groovyShell.evaluate(script);
    }

}
