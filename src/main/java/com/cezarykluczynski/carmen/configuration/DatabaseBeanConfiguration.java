package com.cezarykluczynski.carmen.configuration;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
@ComponentScan("com.cezarykluczynski.carmen")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.cezarykluczynski.carmen")
public class DatabaseBeanConfiguration {

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext ctx;

    @Bean(destroyMethod = "close")
    public DataSource myDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setDriverClassName(env.getProperty("postgres.driverClassName"));
        basicDataSource.setUrl(env.getProperty("postgres.url"));
        basicDataSource.setUsername(env.getProperty("postgres.username"));
        basicDataSource.setPassword(env.getProperty("postgres.password"));
        basicDataSource.setValidationQuery("SELECT 1;");

        return basicDataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(ctx.getBean(DataSource.class));
        lef.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        lef.setPackagesToScan("com.cezarykluczynski.carmen");
        lef.setPersistenceUnitName("SpringDataJPA");
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        properties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        lef.setJpaProperties(properties);
        lef.afterPropertiesSet();
        return lef.getObject();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

}
