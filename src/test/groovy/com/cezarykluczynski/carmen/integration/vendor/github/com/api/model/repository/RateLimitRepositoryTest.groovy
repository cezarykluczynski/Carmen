package com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO
import com.cezarykluczynski.carmen.model.github.RateLimit
import org.springframework.beans.factory.annotation.Autowired

import javax.persistence.EntityManager

class RateLimitRepositoryTest extends IntegrationTest {

    @Autowired
    private RateLimitRepository rateLimitRepository

    @Autowired
    private RateLimitRepositoryFixtures rateLimitRepositoryFixtures

    @Autowired
    private EntityManager entityManager

    def "entity is created"() {
        given:
        RateLimitDTO RateLimitDTO = new RateLimitDTO(
            "core",
            5000,
            4999,
            new Date()
        )

        when:
        RateLimit rateLimitEntity = rateLimitRepository.create RateLimitDTO
        long differenceInDates = (new Date()).getTime() - rateLimitEntity.getReset().getTime()

        then:
        rateLimitEntity.getResource() == "core"
        rateLimitEntity.getLimit() == 5000
        rateLimitEntity.getRemaining() == 4999
        differenceInDates >= 0 && differenceInDates <= 10000

        cleanup:
        rateLimitRepository.delete rateLimitEntity
    }

    def "core limit exists"() {
        given:
        RateLimit rateLimitEntityMock = rateLimitRepositoryFixtures.createRateLimitEntityExpiringIn1Second "core"

        when:
        RateLimit rateLimitEntityExpected = rateLimitRepository.getCoreLimit()

        then:
        rateLimitEntityMock.getId() == rateLimitEntityExpected.getId()

        cleanup:
        rateLimitRepository.delete rateLimitEntityMock
    }

    def "search limit exists"() {
        given:
        RateLimit rateLimitEntityMock = rateLimitRepositoryFixtures.createRateLimitEntityExpiringIn1Second "search"

        when:
        RateLimit rateLimitEntityExpected = rateLimitRepository.getSearchLimit()

        then:
        rateLimitEntityMock.getId() == rateLimitEntityExpected.getId()

        cleanup:
        rateLimitRepository.delete rateLimitEntityMock
    }

    def "remaining counter is decremented"() {
        given:
        RateLimit rateLimitEntityMock = rateLimitRepositoryFixtures.createRateLimitEntityExpiringIn1Second "core"
        Integer previousRemaining = rateLimitEntityMock.getRemaining()

        when:
        rateLimitRepository.decrementRateLimitRemainingCounter()

        RateLimit rateLimitEntityMockUpdated = (RateLimit) entityManager
            .createQuery("from github.RateLimit as r where r.id = :rateLimitId")
            .setParameter("rateLimitId", rateLimitEntityMock.getId())
            .resultList[0]

        then:
        previousRemaining - 1 == rateLimitEntityMockUpdated.getRemaining()
    }

    def "old limits gets deleted"() {
        given:
        RateLimit rateLimitEntityOldMock = rateLimitRepositoryFixtures.createRateLimitEntityExpiringIn1Second "core"
        Long rateLimitEntityOldMockId = rateLimitEntityOldMock.getId()
        Thread.sleep 1100
        RateLimit rateLimitEntityCurrentMock = rateLimitRepositoryFixtures.createRateLimitEntityExpiringIn1Second "core"

        when:
        rateLimitRepository.deleteOldLimits "core"

        List<RateLimit> rateLimitEntityOldMockList = entityManager
            .createQuery("from github.RateLimit as r where r.id = :rateLimitId")
            .setParameter("rateLimitId", rateLimitEntityOldMockId)
            .resultList
        List<RateLimit> rateLimitEntityCurrentMockList = entityManager
            .createQuery("from github.RateLimit as r where r.resource = :resource")
            .setParameter("resource", "core")
            .resultList

        then:
        rateLimitEntityOldMockList.size() == 0
        rateLimitEntityCurrentMockList.size() == 1

        cleanup:
        rateLimitRepository.delete rateLimitEntityCurrentMock
    }

}
