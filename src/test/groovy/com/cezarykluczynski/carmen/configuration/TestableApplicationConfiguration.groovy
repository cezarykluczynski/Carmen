package com.cezarykluczynski.carmen.configuration

import com.cezarykluczynski.carmen.cron.model.repository.CronRepository
import com.cezarykluczynski.carmen.cron.model.repository.CronRepositoryFixtures
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository.RateLimitRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository.RateLimitRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.*
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.RepositoriesRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.*
import com.cezarykluczynski.carmen.vcs.server.Server
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

import javax.persistence.EntityManager

@Configuration
@Import([
    ApplicationConfiguration.class
])
class TestableApplicationConfiguration {

    @Autowired
    private ApplicationContext ctx

    @Bean
    public PendingRequestRepositoryFixtures pendingRequestRepositoryFixtures() {
        return new PendingRequestRepositoryFixtures(ctx.getBean(PendingRequestRepository))
    }

    @Bean
    public RateLimitRepositoryFixtures rateLimitRepositoryFixtures() {
        return new RateLimitRepositoryFixtures(ctx.getBean(RateLimitRepository))
    }

    @Bean
    public RepositoriesRepositoryFixtures repositoriesRepositoryFixtures() {
        return new RepositoriesRepositoryFixtures(ctx.getBean(RepositoriesRepository))
    }

    @Bean
    public UserRepositoryFixtures userRepositoryFixtures() {
        return new UserRepositoryFixtures(ctx.getBean(UserRepository))
    }

    @Bean
    public UserFollowersRepositoryFixtures userFollowersRepositoryFixtures() {
        return new UserFollowersRepositoryFixtures(ctx.getBean(UserFollowersRepository))
    }

    @Bean
    public UserFollowingRepositoryFixtures userFollowingRepositoryFixtures() {
        return new UserFollowingRepositoryFixtures(ctx.getBean(UserFollowingRepository))
    }

    @Bean
    public CronRepositoryFixtures cronRepositoryFixtures() {
        return new CronRepositoryFixtures(ctx.getBean(CronRepository))
    }

    @Bean
    public RepositoryCloneRepositoryFixtures repositoryCloneRepositoryFixtures() {
        return new RepositoryCloneRepositoryFixtures(ctx.getBean(UserRepositoryFixtures),
                ctx.getBean(RepositoryCloneRepository), ctx.getBean(RepositoryRepository), ctx.getBean(Server),
                ctx.getBean(EntityManager))
    }

    @Bean RepositoryRepositoryFixtures repositoryRepositoryFixtures() {
        return new RepositoryRepositoryFixtures(ctx.getBean(RepositoryRepository))
    }

}
