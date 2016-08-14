package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.fixture.org.hibernate.SessionFactoryFixtures
import com.cezarykluczynski.carmen.model.github.RateLimit
import com.cezarykluczynski.carmen.set.github.RateLimit as RateLimitSet
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired

class RateLimitDAOImplTest extends IntegrationTest {

    private RateLimitDAOImpl rateLimitDAOImpl

    @Autowired
    private RateLimitDAOImplFixtures githubRateLimitDAOImplFixtures

    @Autowired
    private SessionFactory sessionFactory

    def setup() {
        rateLimitDAOImpl = new RateLimitDAOImpl(sessionFactory)
    }

    def "entity is created"() {
        given:
        RateLimitSet rateLimitSet = new RateLimitSet(
            "core",
            5000,
            4999,
            new Date()
        )

        when:
        RateLimit rateLimitEntity = rateLimitDAOImpl.create rateLimitSet
        long differenceInDates = (new Date()).getTime() - rateLimitEntity.getReset().getTime()

        then:
        rateLimitEntity.getResource() == "core"
        rateLimitEntity.getLimit() == 5000
        rateLimitEntity.getRemaining() == 4999
        differenceInDates >= 0 && differenceInDates <= 10000

        cleanup:
        rateLimitDAOImpl.delete rateLimitEntity
    }

    def "core limit exists"() {
        given:
        RateLimit rateLimitEntityMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"

        when:
        RateLimit rateLimitEntityExpected = rateLimitDAOImpl.getCoreLimit()

        then:
        rateLimitEntityMock.getId() == rateLimitEntityExpected.getId()

        cleanup:
        rateLimitDAOImpl.delete rateLimitEntityMock
    }

    def "core limit does not exists"() {
        given:
        SessionFactory sessionFactoryMock = SessionFactoryFixtures.createSessionFactoryMockWithEmptyCriteriaList RateLimit.class
        setSessionFactoryToDao rateLimitDAOImpl, sessionFactoryMock

        when:
        def success = true
        try {
            // exercise
            rateLimitDAOImpl.getCoreLimit()

            success = false
        } catch (Throwable e) {
        }

        then:
        success

        cleanup:
        setSessionFactoryToDao rateLimitDAOImpl, sessionFactory
    }

    def "search limit exists"() {
        given:
        RateLimit rateLimitEntityMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "search"

        when:
        RateLimit rateLimitEntityExpected = rateLimitDAOImpl.getSearchLimit()

        then:
        rateLimitEntityMock.getId() == rateLimitEntityExpected.getId()

        cleanup:
        rateLimitDAOImpl.delete rateLimitEntityMock
    }

    def "search limit does not exists"() {
        given:
        SessionFactory sessionFactoryMock = SessionFactoryFixtures.createSessionFactoryMockWithEmptyCriteriaList RateLimit.class
        setSessionFactoryToDao rateLimitDAOImpl, sessionFactoryMock

        when:
        def success = true
        try {
            // exercise
            rateLimitDAOImpl.getSearchLimit()

            success = false
        } catch (Throwable e) {
        }

        then:
        success

        cleanup:
        setSessionFactoryToDao rateLimitDAOImpl, sessionFactory
    }

    def "remaining counter is decremented"() {
        given:
        RateLimit rateLimitEntityMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"
        Integer previousRemaining = rateLimitEntityMock.getRemaining()

        when:
        rateLimitDAOImpl.decrementRateLimitRemainingCounter()

        Session session = sessionFactory.openSession()
        RateLimit rateLimitEntityMockUpdated = (RateLimit) session
            .createQuery("from github.RateLimit as r where r.id = :rateLimitId")
            .setLong("rateLimitId", rateLimitEntityMock.getId())
            .list()
            .get(0)
        session.close()

        then:
        previousRemaining - 1 == rateLimitEntityMockUpdated.getRemaining()
    }

    def "old limits gets deleted"() {
        given:
        RateLimit rateLimitEntityOldMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"
        Long rateLimitEntityOldMockId = rateLimitEntityOldMock.getId()
        Thread.sleep 1100
        RateLimit rateLimitEntityCurrentMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"

        when:
        rateLimitDAOImpl.deleteOldLimits "core"

        Session session = sessionFactory.openSession()
        List<RateLimit> rateLimitEntityOldMockList = session
            .createQuery("from github.RateLimit as r where r.id = :rateLimitId")
            .setLong("rateLimitId", rateLimitEntityOldMockId)
            .list()
        List<RateLimit> rateLimitEntityCurrentMockList = session
            .createQuery("from github.RateLimit as r where r.resource = :resource")
            .setString("resource", "core")
            .list()
        session.close()

        then:
        rateLimitEntityOldMockList.size() == 0
        rateLimitEntityCurrentMockList.size() == 1

        cleanup:
        rateLimitDAOImpl.delete rateLimitEntityCurrentMock
    }

    private static void setSessionFactoryToDao(RateLimitDAOImpl rateLimitDAOImpl, SessionFactory sessionFactory) {
        rateLimitDAOImpl.sessionFactory = sessionFactory
    }

}
