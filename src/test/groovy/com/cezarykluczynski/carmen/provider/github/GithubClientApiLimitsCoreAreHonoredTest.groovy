package com.cezarykluczynski.carmen.client.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.github.RateLimit
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.client.github.GithubRateLimitExceededException
import com.cezarykluczynski.carmen.dao.github.RateLimitDAO
import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImplFixtures

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
public class GithubClientApiLimitsCoreAreHonoredTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GithubClient githubClient

    @Autowired
    private RateLimitDAO rateLimitDAOImpl

    @Autowired
    RateLimitDAOImplFixtures githubRateLimitDAOImplFixtures

    RateLimit mockRateLimitEntity

    @BeforeMethod
    void setup() {
        mockRateLimitEntity = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"
    }

    // assertion: annotated
    @Test(expectedExceptions = GithubRateLimitExceededException.class)
    void apiLimitsAreHonored() throws Exception {
        // exercise
        githubClient.getUser "cezarykluczynski"
    }

    @AfterMethod
    void teardown() {
        githubRateLimitDAOImplFixtures.deleteRateLimitEntity mockRateLimitEntity
    }

}
