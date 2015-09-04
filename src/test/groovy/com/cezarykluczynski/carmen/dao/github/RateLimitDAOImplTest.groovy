package com.cezarykluczynski.carmen.dao.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.RateLimitDAOImpl
import com.cezarykluczynski.carmen.model.github.RateLimit
import com.cezarykluczynski.carmen.set.github.RateLimit as RateLimitSet

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
    private RateLimitDAOImpl rateLimitDAOImpl

    @Test
    void create() {
        // setup
        RateLimitSet rateLimitSet = new RateLimitSet(
            "core",
            5000,
            4999,
            new Date()
        )

        RateLimit rateLimitEntity = rateLimitDAOImpl.create rateLimitSet

        Assert.assertEquals rateLimitEntity.getResource(), "core"
        Assert.assertEquals rateLimitEntity.getLimit(), 5000
        Assert.assertEquals rateLimitEntity.getRemaining(), 4999
        long differenceInDates = (new Date()).getTime() - rateLimitEntity.getReset().getTime()
        Assert.assertTrue differenceInDates >= 0 && differenceInDates <= 10000

        // teardown
        rateLimitDAOImpl.delete rateLimitEntity
    }

}
