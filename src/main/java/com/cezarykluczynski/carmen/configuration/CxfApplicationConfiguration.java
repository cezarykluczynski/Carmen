package com.cezarykluczynski.carmen.configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.Lists;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.Path;

@Configuration
@ComponentScan("com.cezarykluczynski.carmen")
public class CxfApplicationConfiguration {

    @Autowired
    private ApplicationContext ctx;

    @Bean
    public ServletRegistrationBean cxfServletRegistrationBean() {
        return new ServletRegistrationBean(new CXFServlet(), "/api/*");
    }

    @Bean
    public Server cxfServer() {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(ctx.getBean(SpringBus.class));
        factory.setProviders(Lists.newArrayList(new JacksonJsonProvider()));
        factory.setServiceBeans(Lists.newArrayList(ctx.getBeansWithAnnotation(Path.class).values()));
        return factory.create();
    }

    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }

}
