package org.qamock.config;

import org.qamock.dynamic.script.GroovyScriptHandler;
import org.qamock.dynamic.script.ScriptUtilsImpl;
import org.qamock.service.DynamicResourcesService;
import org.qamock.service.ScriptService;
import org.qamock.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicResourceConfig {

    @Autowired
    ScriptService scriptService;

    @Autowired
    SequenceService sequenceService;

    @Bean
    public ScriptUtilsImpl scriptUtils(){
        if(scriptService == null || sequenceService == null){
            System.out.println("ScriptUtilsImpl incorrect initialize =(");
            System.out.println("ScriptService = " + (scriptService == null ? "null" : "OK"));
            System.out.println("SequenceService = " + (sequenceService == null ? "null" : "OK"));
        }
        return new ScriptUtilsImpl(scriptService, sequenceService);
    }

    @Bean
    public GroovyScriptHandler scriptHandler(){
        GroovyScriptHandler groovyScriptHandler = new GroovyScriptHandler();
        groovyScriptHandler.setScriptUtils(scriptUtils());
        return groovyScriptHandler;
    }

}
