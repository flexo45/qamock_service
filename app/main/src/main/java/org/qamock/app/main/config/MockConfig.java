package org.qamock.app.main.config;

import org.qamock.app.main.script.handler.ScriptSuiteProcessorImpl;
import org.qamock.app.main.xml.XmlProcessor;
import org.qamock.app.main.xml.XmlProcessorImpl;
import org.qamock.app.main.script.handler.ScriptSuiteProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;

@Configuration
public class MockConfig {

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
        castorMarshaller.setPackagesToScan("org.qamock.app.main.xml.object");
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
