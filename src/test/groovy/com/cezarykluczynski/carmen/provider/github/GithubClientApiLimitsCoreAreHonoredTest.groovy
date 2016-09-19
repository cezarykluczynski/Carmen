package com.cezarykluczynski.carmen.provider.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.client.github.GithubRateLimitExceededException
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository.RateLimitRepositoryFixtures
import com.cezarykluczynski.carmen.model.github.RateLimit
import org.springframework.beans.factory.annotation.Autowired

public class GithubClientApiLimitsCoreAreHonoredTest extends IntegrationTest {

    @Autowired
    private GithubClient githubClient

    @Autowired
    private RateLimitRepositoryFixtures rateLimitRepositoryFixtures

    private RateLimit mockRateLimitEntity

    def setup() {
        mockRateLimitEntity = rateLimitRepositoryFixtures.createRateLimitEntityExpiringIn1Second "core"
    }

    def cleanup() {
        rateLimitRepositoryFixtures.deleteRateLimitEntity mockRateLimitEntity
    }

    def "api limits are honored"() {
        when:
        githubClient.getUser "cezarykluczynski"

        then:
        thrown GithubRateLimitExceededException
    }

}
