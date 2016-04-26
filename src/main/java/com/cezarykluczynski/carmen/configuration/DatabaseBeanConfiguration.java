package com.cezarykluczynski.carmen.configuration;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource(value = { "classpath:config.properties" })
@ComponentScan("com.cezarykluczynski.carmen")
@EnableTransactionManagement
public class DatabaseBeanConfiguration  {

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext ctx;

    @Bean(destroyMethod = "close")
    BasicDataSource myDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setDriverClassName(env.getProperty("postgres.driverClassName"));
        basicDataSource.setUrl(env.getProperty("postgres.url"));
        basicDataSource.setUsername(env.getProperty("postgres.username"));
        basicDataSource.setPassword(env.getProperty("postgres.password"));

        return basicDataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();

        localSessionFactoryBean.setDataSource((BasicDataSource) ctx.getBean("myDataSource"));
        localSessionFactoryBean.setPackagesToScan("com.cezarykluczynski.carmen.model");

        return localSessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();

        hibernateTransactionManager.setSessionFactory(getSessionFactoryBean());

        return hibernateTransactionManager;
    }

    private SessionFactory getSessionFactoryBean() {
        return (SessionFactory) ctx.getBean("sessionFactory");
    }

}
