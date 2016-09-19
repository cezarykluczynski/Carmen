package com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository

import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO
import com.cezarykluczynski.carmen.model.github.RateLimit

class RateLimitRepositoryFixtures {

    private RateLimitRepository rateLimitRepository

    public RateLimitRepositoryFixtures(RateLimitRepository rateLimitRepository) {
        this.rateLimitRepository = rateLimitRepository
    }

    RateLimit createRateLimitEntityExpiringIn1Second(String resource) {
        RateLimitDTO mockRateLimitSet = createRateLimitSetExpiringIn1Second resource
        rateLimitRepository.create mockRateLimitSet
    }

    private static RateLimitDTO createRateLimitSetExpiringIn1Second(String resource) {
        Calendar calendar = Calendar.getInstance()
        calendar.setTime new Date()
        /**
         * Create limit that will expire in one second,
         * so we're fairly sure it will be picked by GithubClient.
         */
        Date reset = new Date(calendar.getTimeInMillis() + 1000 * 60 * 60 - 1000)

        /** Limit has to be bellow 10. The 50 part is irrelevant at this point. */
        RateLimitDTO mockRateLimitSet = new RateLimitDTO(resource, 50, 9, reset)
        return mockRateLimitSet
    }

    void deleteRateLimitEntity(RateLimit rateLimitEntity) {
        rateLimitRepository.delete rateLimitEntity
    }

}
