package com.cezarykluczynski.carmen.configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.Maps;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.spring.SpringResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping;

import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

@Configuration
@ComponentScan("com.cezarykluczynski.carmen")
public class CxfApplicationConfiguration {

    @Autowired
    private ApplicationContext ctx;

    @Bean
    public Server cxfServer() {
        LinkedList<ResourceProvider> resourceProviders = new LinkedList<>();
        for (String beanName : ctx.getBeanDefinitionNames()) {
            if (ctx.findAnnotationOnBean(beanName, Path.class) != null) {
                SpringResourceFactory factory = new SpringResourceFactory(beanName);
                factory.setApplicationContext(ctx);
                resourceProviders.add(factory);
            }
        }

        Map<Object, Object> extensionMappings = Maps.newHashMap();
        extensionMappings.put("json", "application/json");

        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBindingId(JAXRSBindingFactory.JAXRS_BINDING_ID);
        factory.setAddress("/");
        factory.setBus(ctx.getBean(SpringBus.class));
        factory.setProviders(Arrays.asList(new JacksonJsonProvider()));
        factory.setResourceProviders(resourceProviders);
        factory.setExtensionMappings(extensionMappings);
        return factory.create();
    }

    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    public ControllerClassNameHandlerMapping controllerClassNameHandlerMapping() {
        return new ControllerClassNameHandlerMapping();
    }

    @Bean
    public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter() {
        return new AnnotationMethodHandlerAdapter();
    }

}
