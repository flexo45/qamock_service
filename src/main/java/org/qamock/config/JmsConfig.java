package org.qamock.config;

import com.sun.messaging.QueueConnectionFactory;
import org.qamock.xml.object.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.jms.ConnectionFactory;

@EnableJms
@Configuration
public class JmsConfig {

    private static final Logger logger = LoggerFactory.getLogger(JmsConfig.class);

    @Bean
    public QueueConnectionFactory connectionFactoryGlassFish(){
        QueueConnectionFactory queueConnectionFactory = new QueueConnectionFactory();

        for(String name : queueConnectionFactory.getConfiguration().stringPropertyNames()){
            logger.info("QueueConnectionFactory config name=" + name + ", value=" + queueConnectionFactory.getConfiguration().getProperty(name));
        }

        return new QueueConnectionFactory();
    }

    @Bean
    @Primary
    public CachingConnectionFactory cachingConnectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(connectionFactoryGlassFish());
        return cachingConnectionFactory;
    }

    /*
    @Bean
    public JmsTemplate jmsTemplateGF(){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactoryGlassFish());
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplateCachingGF(){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(cachingConnectionFactory());
        return jmsTemplate;
    }
    */

    /*@Bean
    public JmsListenerContainerFactory<?> factoryBean(ConnectionFactory connectionFactory,
                                                  DefaultJmsListenerContainerFactoryConfigurer configurer){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // You could still override some of Boot's default if necessary.
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return  factory;
    }*/
}
