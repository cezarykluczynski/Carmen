package com.cezarykluczynski.carmen.dao.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.RateLimitDAO
import com.cezarykluczynski.carmen.model.github.RateLimit
import com.cezarykluczynski.carmen.set.github.RateLimit as RateLimitSet
import com.cezarykluczynski.carmen.fixture.org.hibernate.SessionFactoryFixtures

import org.testng.annotations.Test

import org.testng.Assert

import java.util.Date

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class RateLimitDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private RateLimitDAO rateLimitDAOImpl

    @Autowired
    RateLimitDAOImplFixtures githubRateLimitDAOImplFixtures

    @Autowired
    SessionFactoryFixtures sessionFactoryFixtures

    @Autowired
    private SessionFactory sessionFactory

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Test
    void create() {
        // setup
        RateLimitSet rateLimitSet = new RateLimitSet(
            "core",
            5000,
            4999,
            new Date()
        )

        // exercise
        RateLimit rateLimitEntity = rateLimitDAOImpl.create rateLimitSet

        // assertion
        Assert.assertEquals rateLimitEntity.getResource(), "core"
        Assert.assertEquals rateLimitEntity.getLimit(), 5000
        Assert.assertEquals rateLimitEntity.getRemaining(), 4999
        long differenceInDates = (new Date()).getTime() - rateLimitEntity.getReset().getTime()
        Assert.assertTrue differenceInDates >= 0 && differenceInDates <= 10000

        // teardown
        rateLimitDAOImpl.delete rateLimitEntity
    }

    @Test
    void getCoreLimitExisting() {
        // setup
        RateLimit rateLimitEntityMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"

        // exercise
        RateLimit rateLimitEntityExpected = rateLimitDAOImpl.getCoreLimit()

        // assertion
        Assert.assertEquals rateLimitEntityMock.getId(), rateLimitEntityExpected.getId()

        // teardown
        rateLimitDAOImpl.delete rateLimitEntityMock
    }

    @Test
    void getCoreLimitNotExisting() {
        // setup
        SessionFactory sessionFactoryMock = sessionFactoryFixtures.createSessionFactoryMockWithEmptyCriteriaList RateLimit.class
        rateLimitDAOImpl.setSessionFactory sessionFactoryMock

        try {
            // exercise
            rateLimitDAOImpl.getCoreLimit()

            // assertion
            Assert.assertTrue false
        } catch (Throwable e) {
            // assertion
            Assert.assertTrue e instanceof NullPointerException
        }

        // teardown
        rateLimitDAOImpl.setSessionFactory sessionFactory
    }

    @Test
    void getSearchLimitExisting() {
        // setup
        RateLimit rateLimitEntityMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "search"

        // exercise
        RateLimit rateLimitEntityExpected = rateLimitDAOImpl.getSearchLimit()

        // assertion
        Assert.assertEquals rateLimitEntityMock.getId(), rateLimitEntityExpected.getId()

        // teardown
        rateLimitDAOImpl.delete rateLimitEntityMock
    }

    @Test
    void getSearchLimitNotExisting() {
        // setup
        SessionFactory sessionFactoryMock = sessionFactoryFixtures.createSessionFactoryMockWithEmptyCriteriaList RateLimit.class
        rateLimitDAOImpl.setSessionFactory sessionFactoryMock

        try {
            // exercise
            rateLimitDAOImpl.getSearchLimit()

            // assertion
            Assert.assertTrue false
        } catch (Throwable e) {
            // assertion
            Assert.assertTrue e instanceof NullPointerException
        }

        // teardown
        rateLimitDAOImpl.setSessionFactory sessionFactory
    }

    @Test
    void decrementRateLimitRemainingCounter() {
        // setup
        RateLimit rateLimitEntityMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"
        Integer previousRemaining = rateLimitEntityMock.getRemaining()

        // exercise
        rateLimitDAOImpl.decrementRateLimitRemainingCounter()

        // assertion
        Session session = sessionFactory.openSession()
        RateLimit rateLimitEntityMockUpdated = (RateLimit) session
            .createQuery("from github.RateLimit as r where r.id = :rateLimitId")
            .setLong("rateLimitId", rateLimitEntityMock.getId())
            .list()
            .get(0)
        session.close()

        Assert.assertEquals previousRemaining - 1, rateLimitEntityMockUpdated.getRemaining()

        // teardown
        rateLimitDAOImpl.setSessionFactory sessionFactory
    }

    @Test
    void deleteOldLimits() {
        // setup
        RateLimit rateLimitEntityOldMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"
        Long rateLimitEntityOldMockId = rateLimitEntityOldMock.getId()
        Thread.sleep 1100
        RateLimit rateLimitEntityCurrentMock = githubRateLimitDAOImplFixtures.createRateLimitEntityExpiringIn1Second "core"

        // exercise
        rateLimitDAOImpl.deleteOldLimits "core"

        // assertion
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

        Assert.assertEquals rateLimitEntityOldMockList.size(), 0
        Assert.assertEquals rateLimitEntityCurrentMockList.size(), 1

        // teardown
        rateLimitDAOImpl.delete rateLimitEntityCurrentMock
    }

}
