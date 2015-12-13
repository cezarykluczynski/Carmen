package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.RateLimit;

public interface RateLimitDAO {

    RateLimit create(com.cezarykluczynski.carmen.set.github.RateLimit rateLimit);

    RateLimit getCoreLimit();

    RateLimit getSearchLimit();

    void decrementRateLimitRemainingCounter();

    void deleteOldLimits(String resource);

    void delete(RateLimit rateLimitEntity);

}
