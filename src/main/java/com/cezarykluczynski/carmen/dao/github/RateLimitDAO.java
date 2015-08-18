package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.RateLimit;

public interface RateLimitDAO {

    public RateLimit create(com.cezarykluczynski.carmen.set.github.RateLimit rateLimit);

    public RateLimit getCoreLimit();

    public void decrementRateLimitRemainingCounter();

    public RateLimit getSearchLimit();

    public void deleteOldLimits(String resource);
}
