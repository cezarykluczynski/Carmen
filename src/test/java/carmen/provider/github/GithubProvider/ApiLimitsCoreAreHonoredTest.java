package carmen.web.GithubProvider;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import carmen.provider.github.GithubProvider;
import carmen.provider.github.GithubRateLimitExceededException;
import carmen.set.github.RateLimit;
import carmen.dao.github.RateLimitDAO;

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/database-config.xml", "classpath:spring/mvc-core-config.xml" })
public class ApiLimitsCoreAreHonoredTest {

    @Autowired
    GithubProvider githubProvider;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RateLimitDAO rateLimitDAOImpl;

    private carmen.model.github.RateLimit mockRateLimitEntity;

    @Before
    public void setup() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        /**
         * Create limit that will expire in one second,
         * so we're fairlu sure it will be picked by GithubProvider.
         */
        Date reset = new Date(calendar.getTimeInMillis() + 1000 * 60 * 60 - 1000);

        /** Limit has to be bellow 10. The 50 part is irrelevant at this point. */
        RateLimit mockRateLimitSet = new RateLimit("core", 50, 9, reset);
        mockRateLimitEntity = rateLimitDAOImpl.create(mockRateLimitSet);
    }

    @Test(expected = GithubRateLimitExceededException.class)
    public void apiLimitsAreHonored() throws Exception {
        githubProvider.getUser("cezarykluczynski");
    }

    @After
    public void teardown() {
        Session session = sessionFactory.openSession();

        session.createQuery("delete from github.RateLimit r where r.id = :mockRateLimitEntityId")
            .setLong("mockRateLimitEntityId", mockRateLimitEntity.getId())
            .executeUpdate();

        session.close();
    }
}
