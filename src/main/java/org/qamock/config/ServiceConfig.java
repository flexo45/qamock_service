package org.qamock.config;

import org.qamock.script.data.ScriptsDaoWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;

@Configuration
public class ServiceConfig {

    @Bean
    public ScriptsDaoWrapper scriptDataSource(){
        ScriptsDaoWrapper scriptsDaoGroovy = new ScriptsDaoWrapper();
        scriptsDaoGroovy.setScriptLocation("resources" + File.separator + "scripts");
        return scriptsDaoGroovy;
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(25);
        return threadPoolTaskExecutor;
    }
}
