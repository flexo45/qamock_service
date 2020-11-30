package org.qamock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
//@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class})
@EnableJms
public class WebApplication
{
    public static void main(String[] args) throws Exception{
        SpringApplication app = new SpringApplication(WebApplication.class);
        ConfigurableApplicationContext context = app.run(args);
    }
}
