package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.client.github.GithubRateLimitExceededException
import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.RateLimit
import org.springframework.beans.factory.annotation.Autowired

public class GithubClientApiLimitsCoreAreHonoredTest extends IntegrationTest {

    @Autowired
    private GithubClient githubClient

    @Autowired
    private RateLimitDAOImplFixtures githubRateLimitDAOImplFixtures

    private RateLimit mockRateLimitEntity

    def setup() {
        mockRateLimitEntity = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"
    }

    def cleanup() {
        githubRateLimitDAOImplFixtures.deleteRateLimitEntity mockRateLimitEntity
    }

    def "api limits are honored"() {
        when:
        githubClient.getUser "cezarykluczynski"

        then:
        thrown GithubRateLimitExceededException
    }

}
