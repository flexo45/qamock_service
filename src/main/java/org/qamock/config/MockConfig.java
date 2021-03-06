package org.qamock.config;

import org.qamock.dao.DynamicResourceDao;
import org.qamock.dynamic.DynamicResourceRequestHandler;
import org.qamock.dynamic.DynamicResourceRequestHandlerImpl;
import org.qamock.dynamic.context.TtlHashMap;
import org.qamock.dynamic.script.GroovyScriptHandler;
import org.qamock.dynamic.script.ScriptHandler;
import org.qamock.dynamic.script.ScriptUtils;
import org.qamock.dynamic.script.ScriptUtilsImpl;
import org.qamock.script.ScriptExecutorUtilImpl;
import org.qamock.script.SequenceUtilImpl;
import org.qamock.script.handler.ScriptSuiteProcessor;
import org.qamock.script.handler.ScriptSuiteProcessorImpl;
import org.qamock.xml.XmlProcessor;
import org.qamock.xml.XmlProcessorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class MockConfig {

    @Autowired
    DynamicResourceDao resourceDao;

    @Bean
    public ScriptUtils scriptUtils() {
        final ScriptUtilsImpl scriptUtils = new ScriptUtilsImpl();
        scriptUtils.setScriptExecutorUtils(new ScriptExecutorUtilImpl());
        scriptUtils.setSequenceUtil(new SequenceUtilImpl(resourceDao));
        return scriptUtils;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        return taskExecutor;
    }

    @Bean
    public Jaxb2Marshaller castorMarshaller() {
        final Jaxb2Marshaller castorMarshaller = new Jaxb2Marshaller();
        castorMarshaller.setPackagesToScan("org.qamock.xml.object");
        castorMarshaller.setMarshallerProperties(new HashMap<String, Object>(){
            {
                put("jaxb.formatted.output", true);
            }
        });
        return castorMarshaller;
    }

    @Bean
    public ScriptSuiteProcessor suiteProcessor() {
        final ScriptSuiteProcessorImpl suiteProcessor = new ScriptSuiteProcessorImpl();
        suiteProcessor.setXmlProcessor(xmlProcessor());
        return suiteProcessor;
    }

    @Bean
    public XmlProcessor xmlProcessor() {
        final XmlProcessorImpl xmlProcessor = new XmlProcessorImpl();
        xmlProcessor.setMarshaller(castorMarshaller());
        xmlProcessor.setUnmarshaller(castorMarshaller());
        return xmlProcessor;
    }

    @Bean
    public JavaMailSender mailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("");
        mailSender.setDefaultEncoding("UTF-8");
        return mailSender;
    }
}
