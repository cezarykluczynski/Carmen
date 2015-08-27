package com.cezarykluczynski.carmen.provider.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.cezarykluczynski.carmen.model.github.RateLimit
import com.cezarykluczynski.carmen.provider.github.GithubProvider
import com.cezarykluczynski.carmen.provider.github.GithubRateLimitExceededException
import com.cezarykluczynski.carmen.set.github.RateLimit as RateLimitSet
import com.cezarykluczynski.carmen.dao.github.RateLimitDAO

import java.util.Calendar
import java.util.Date

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
public class ApiLimitsCoreAreHonoredTest {

    @Autowired
    GithubProvider githubProvider

    @Autowired
    private SessionFactory sessionFactory

    @Autowired
    private RateLimitDAO rateLimitDAOImpl

    RateLimit mockRateLimitEntity

    @Before
    void setup() {
        Calendar calendar = Calendar.getInstance()
        calendar.setTime new Date()
        /**
         * Create limit that will expire in one second,
         * so we're fairly sure it will be picked by GithubProvider.
         */
        Date reset = new Date(calendar.getTimeInMillis() + 1000 * 60 * 60 - 1000)

        /** Limit has to be bellow 10. The 50 part is irrelevant at this point. */
        RateLimitSet mockRateLimitSet = new RateLimitSet("core", 50, 9, reset)
        mockRateLimitEntity = rateLimitDAOImpl.create mockRateLimitSet
    }

    @Test(expected = GithubRateLimitExceededException.class)
    void apiLimitsAreHonored() throws Exception {
        githubProvider.getUser "cezarykluczynski"
    }

    @After
    void teardown() {
        Session session = sessionFactory.openSession()

        session.createQuery("delete from github.RateLimit r where r.id = :mockRateLimitEntityId")
            .setLong("mockRateLimitEntityId", mockRateLimitEntity.getId())
            .executeUpdate()

        session.close()
    }
}
