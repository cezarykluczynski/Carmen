package com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO;
import com.cezarykluczynski.carmen.model.github.RateLimit;

public interface RateLimitRepositoryCustom {

    RateLimit create(RateLimitDTO rateLimitDTO);

    RateLimit getCoreLimit();

    RateLimit getSearchLimit();

    void decrementRateLimitRemainingCounter();

    void deleteOldLimits(String resource);

}
