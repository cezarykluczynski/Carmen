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
@ComponentScan("com.cezarykluczynski.carmen.model")
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

//    @Bean
//    public UserDAOImpl githubUserDAOImpl() {
//        return new UserDAOImpl(getSessionFactoryBean(), getGitHubClientBean());
//    }

//    @Bean
//    public UserDAOImplFollowersFolloweesLinkerDelegate githubUserFollowersFolloweesLinker() {
//        return new UserDAOImplFollowersFolloweesLinkerDelegate(getSessionFactoryBean());
//    }

//    @Bean
//    public RateLimitDAOImpl rateLimitDAOImpl() {
//        return new RateLimitDAOImpl(getSessionFactoryBean());
//    }

//    @Bean
//    public RepositoriesDAOImpl usersCarmenUserDAOImpl() {
//        return new RepositoriesDAOImpl(getSessionFactoryBean());
//    }

//    @Bean
//    public CarmenUserDAOImpl githubRepositoriesDAOImpl() {
//        return new CarmenUserDAOImpl(getSessionFactoryBean());
//    }

//    @Bean
//    public RepositoriesClonesDAOImpl githubRepositoriesClonesDAOImpl() {
//        return new RepositoriesClonesDAOImpl(getSessionFactoryBean());
//    }

//    @Bean
//    public UserFollowersDAOImpl propagationsUserFollowersDAOImpl() {
//        return new UserFollowersDAOImpl(getSessionFactoryBean());
//    }
//
//    @Bean
//    public UserFollowingDAOImpl propagationsUserFollowingDAOImpl() {
//        return new UserFollowingDAOImpl(getSessionFactoryBean());
//    }

//    @Bean
//    public com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImpl propagationsRepositoriesDAOImpl() {
//        return new com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImpl(getSessionFactoryBean());
//    }

//    @Bean
//    public PendingRequestDAOImpl apiqueuePendingRequestDAOImpl() {
//        return new PendingRequestDAOImpl(getSessionFactoryBean(), getGitHubClientBean());
//    }

//    @Bean
//    public LanguagesDAOImpl publicLanguagesDAOImpl() {
//        return new LanguagesDAOImpl(getSessionFactoryBean());
//    }

//    @Bean
//    public PendingRequestFactory apiqueuePendingRequestFactory() {
//        return new PendingRequestFactory((PendingRequestDAO) ctx.getBean("apiqueuePendingRequestDAOImpl"));
//    }

    private SessionFactory getSessionFactoryBean() {
        return (SessionFactory) ctx.getBean("sessionFactory");
    }

//    private GithubClient getGitHubClientBean() {
//        return (GithubClient) ctx.getBean("githubClient");
//    }

}
