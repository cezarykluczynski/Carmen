package com.cezarykluczynski.carmen.configuration

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.RateLimitDAO
import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAO
import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO as GithubRepositoriesDAO
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAOImplFixtures as GithubRepositoriesDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAO as PropagationsRepositoriesDAO
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImplFixtures as PropagationsRepositoriesDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.dao.pub.CronsDAO
import com.cezarykluczynski.carmen.dao.pub.CronsDAOImplFixtures
import com.cezarykluczynski.carmen.vcs.server.Server
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import([
    ApplicationConfiguration.class
])
class TestableApplicationConfiguration {

    @Autowired
    private ApplicationContext ctx;

    @Bean
    public PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures() {
        return new PendingRequestDAOImplFixtures(ctx.getBean(PendingRequestDAO))
    }

    @Bean
    public RateLimitDAOImplFixtures githubRateLimitDAOImplFixtures() {
        return new RateLimitDAOImplFixtures(ctx.getBean(RateLimitDAO))
    }

    @Bean
    public PropagationsRepositoriesDAOImplFixtures propagationsRepositoriesDAOImplFixtures() {
        return new PropagationsRepositoriesDAOImplFixtures(ctx.getBean(PropagationsRepositoriesDAO))
    }

    @Bean
    public UserDAOImplFixtures githubUserDAOImplFixtures() {
        return new UserDAOImplFixtures(ctx.getBean(UserDAO));
    }

    @Bean
    public GithubRepositoriesDAOImplFixtures githubRepositoriesDAOImplFixtures() {
        return new GithubRepositoriesDAOImplFixtures(ctx.getBean(GithubRepositoriesDAO),
                ctx.getBean(SessionFactory))
    }

    @Bean
    public UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures() {
        return new UserFollowersDAOImplFixtures(ctx.getBean(UserFollowersDAO))
    }

    @Bean
    public UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures() {
        return new UserFollowingDAOImplFixtures(ctx.getBean(UserFollowingDAO))
    }

    @Bean
    public CronsDAOImplFixtures cronsDAOImplFixtures() {
        return new CronsDAOImplFixtures(ctx.getBean(CronsDAO))
    }

    @Bean
    public RepositoriesClonesDAOImplFixtures repositoriesClonesDAOImplFixtures() {
        return new RepositoriesClonesDAOImplFixtures(ctx.getBean(UserDAOImplFixtures),
                ctx.getBean(RepositoriesDAOImplFixtures), ctx.getBean(RepositoriesClonesDAO), ctx.getBean(Server))
    }

}
