package com.cezarykluczynski.carmen

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@ContextConfiguration(
        classes = TestableApplicationConfiguration.class,
        loader = SpringApplicationContextLoader.class,
        locations = ["classpath:applicationContext.xml"]
)
@WebAppConfiguration
@Configuration
@org.springframework.boot.test.IntegrationTest
class AspectIntegrationTest extends Specification {

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        return new TomcatEmbeddedServletContainerFactory()
    }

}
