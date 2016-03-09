package com.cezarykluczynski.carmen.configuration;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.ImportResource;
//import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;

@Configuration
@Import({
    CassandraBeanConfiguration.class,
    CxfApplicationConfiguration.class,
    DatabaseBeanConfiguration.class,
    GitHubClientsBeanConfiguration.class,
    LanguageStatsBeanConfiguration.class,
    MvcApplicationConfiguration.class
})
//@ImportResource({
//    "classpath:applicationContext.xml"
//})
public class ApplicationConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocation(new ClassPathResource("config.properties"));
        return ppc;
    }

}
