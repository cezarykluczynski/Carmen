package com.cezarykluczynski.carmen.configuration;

import com.cezarykluczynski.carmen.model.cassandra.carmen.Commit;
import com.cezarykluczynski.carmen.model.cassandra.carmen.FollowersAndFollowees;
import com.cezarykluczynski.carmen.repository.carmen.CommitAccessor;
import com.cezarykluczynski.carmen.repository.carmen.FollowersAndFolloweesAccessor;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CassandraBeanConfiguration {

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private Environment env;

    @Bean
    public MappingManager mappingManager() {
        Cluster cluster = Cluster.builder()
                .addContactPoint(env.getProperty("cassandra.contactpoints"))
                .withPort(Integer.parseInt(env.getProperty("cassandra.port")))
                .build();

        return new MappingManager(cluster.connect());
    }

    @Bean
    public Mapper<Commit> commitMapper() {
        return ctx.getBean(MappingManager.class).mapper(Commit.class);
    }

    @Bean
    public CommitAccessor commitAccessor() {
        return ctx.getBean(MappingManager.class).createAccessor(CommitAccessor.class);
    }

    @Bean
    public Mapper<FollowersAndFollowees> followersAndFolloweesMapper() {
        return ctx.getBean(MappingManager.class).mapper(FollowersAndFollowees.class);
    }

    @Bean
    public FollowersAndFolloweesAccessor followersAndFolloweesAccessor() {
        return ctx.getBean(MappingManager.class).createAccessor(FollowersAndFolloweesAccessor.class);
    }

}
