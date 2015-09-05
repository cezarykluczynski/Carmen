package com.cezarykluczynski.carmen.provider.github

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
import com.cezarykluczynski.carmen.dao.github.RateLimitDAO
import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImplFixtures

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
public class ApiLimitsCoreAreHonoredTest {

    @Autowired
    GithubProvider githubProvider

    @Autowired
    private RateLimitDAO rateLimitDAOImpl

    @Autowired
    RateLimitDAOImplFixtures githubRateLimitDAOImplFixtures

    RateLimit mockRateLimitEntity

    @Before
    void setup() {
        mockRateLimitEntity = githubRateLimitDAOImplFixtures.createRateLimitExpiringIn1Second()
    }

    @Test(expected = GithubRateLimitExceededException.class)
    void apiLimitsAreHonored() throws Exception {
        githubProvider.getUser "cezarykluczynski"
    }

    @After
    void teardown() {
        githubRateLimitDAOImplFixtures.deleteRateLimitEntity mockRateLimitEntity
    }
}
