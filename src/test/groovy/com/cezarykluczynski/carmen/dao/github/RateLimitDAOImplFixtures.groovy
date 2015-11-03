package com.cezarykluczynski.carmen.dao.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.RateLimit
import com.cezarykluczynski.carmen.set.github.RateLimit as RateLimitSet

import java.util.Calendar
import java.util.Date

@Component
class RateLimitDAOImplFixtures {

    @Autowired
    RateLimitDAO rateLimitDAOImpl

    RateLimit createRateLimitEntityExpiringIn1Second(String resource) {
        RateLimitSet mockRateLimitSet = createRateLimitSetExpiringIn1Second resource
        rateLimitDAOImpl.create mockRateLimitSet
    }

    RateLimitSet createRateLimitSetExpiringIn1Second(String resource) {
        Calendar calendar = Calendar.getInstance()
        calendar.setTime new Date()
        /**
         * Create limit that will expire in one second,
         * so we're fairly sure it will be picked by GithubClient.
         */
        Date reset = new Date(calendar.getTimeInMillis() + 1000 * 60 * 60 - 1000)

        /** Limit has to be bellow 10. The 50 part is irrelevant at this point. */
        RateLimitSet mockRateLimitSet = new RateLimitSet(resource, 50, 9, reset)
        return mockRateLimitSet
    }

    void deleteRateLimitEntity(RateLimit rateLimitEntity) {
        rateLimitDAOImpl.delete rateLimitEntity
    }

}
