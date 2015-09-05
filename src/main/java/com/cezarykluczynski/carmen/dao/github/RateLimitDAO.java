package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.RateLimit;

public interface RateLimitDAO {

    public RateLimit create(com.cezarykluczynski.carmen.set.github.RateLimit rateLimit);

    public RateLimit getCoreLimit();

    public RateLimit getSearchLimit();

    public void decrementRateLimitRemainingCounter();

    public void deleteOldLimits(String resource);

    public void delete(RateLimit rateLimitEntity);

}
