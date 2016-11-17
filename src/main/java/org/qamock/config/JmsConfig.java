package org.qamock.config;

import com.sun.messaging.QueueConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableJms
@Configuration
public class JmsConfig {

    @Bean
    public QueueConnectionFactory connectionFactoryGlassFish(){
        return new QueueConnectionFactory();
        //queueConnectionFactory.setProperty(ConnectionConfiguration.imqAddressList, addressList);
    }

    /*@Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        return jmsTemplate;
    }*/

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
