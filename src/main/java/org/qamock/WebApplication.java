package org.qamock;

import org.hibernate.SessionFactory;
import org.qamock.dao.DynamicResourceDao;
import org.qamock.domain.DynamicResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@Configuration
@SpringBootApplication
public class WebApplication
{
    public static void main(String[] args) throws Exception{
        SpringApplication app = new SpringApplication(WebApplication.class);
        ConfigurableApplicationContext context = app.run(args);
    }
}
