package com.cezarykluczynski.carmen;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.cezarykluczynski.carmen")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new Application().configure(new SpringApplicationBuilder(Application.class)).run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

}