package org.qamock.config;

import org.qamock.script.handler.ScriptSuiteProcessorImpl;
import org.qamock.xml.XmlProcessorImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ServiceConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(25);
        return threadPoolTaskExecutor;
    }

    @Bean
    public Jaxb2Marshaller castorMarshaller(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("org.qamock.xml.object");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("jaxb.formatted.output", true);
        marshaller.setMarshallerProperties(map);
        return marshaller;
    }

    @Bean
    public ScriptSuiteProcessorImpl suiteProcessor(){
        ScriptSuiteProcessorImpl scriptSuiteProcessor = new ScriptSuiteProcessorImpl();
        scriptSuiteProcessor.setXmlProcessor(xmlProcessor());
        return scriptSuiteProcessor;
    }

    @Bean
    public XmlProcessorImpl xmlProcessor(){
        XmlProcessorImpl xmlProcessor = new XmlProcessorImpl();
        xmlProcessor.setMarshaller(castorMarshaller());
        xmlProcessor.setUnmarshaller(castorMarshaller());
        return xmlProcessor;
    }

    @Bean
    public JavaMailSenderImpl mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("post.temamedia.ru");
        mailSender.setDefaultEncoding("UTF-8");
        return mailSender;
    }



}
