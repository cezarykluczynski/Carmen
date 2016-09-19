package com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository;

import com.cezarykluczynski.carmen.model.github.RateLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateLimitRepository extends JpaRepository<RateLimit, Long>, RateLimitRepositoryCustom {

    RateLimit findFirstByResourceOrderByResetDesc(String resource);

}
