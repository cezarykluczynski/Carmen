package com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO;
import com.cezarykluczynski.carmen.model.github.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
public class RateLimitRepositoryImpl implements RateLimitRepositoryCustom {

    @Autowired
    private RateLimitRepository rateLimitRepository;

    private EntityManager entityManager;

    @Autowired
    public RateLimitRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public RateLimit create(RateLimitDTO rateLimitSet) {
        RateLimit rateLimitEntity = new RateLimit();
        rateLimitEntity.setResource(rateLimitSet.getResource());
        rateLimitEntity.setLimit(rateLimitSet.getLimit());
        rateLimitEntity.setRemaining(rateLimitSet.getRemaining());
        rateLimitEntity.setReset(rateLimitSet.getReset());
        rateLimitEntity.setUpdated(new Date());
        return rateLimitRepository.save(rateLimitEntity);
    }

    @Override
    @Transactional
    public RateLimit getCoreLimit() {
        return getLimitByResource("core");
    }

    @Override
    @Transactional
    public RateLimit getSearchLimit() {
        return getLimitByResource("search");
    }

    @Override
    @Transactional
    public void decrementRateLimitRemainingCounter() {
        entityManager.createQuery("update github.RateLimit set remaining = remaining - 1").executeUpdate();
    }

    @Override
    @Transactional
    public void deleteOldLimits(String resource) {
        RateLimit currentLimit = getCoreLimit();

        entityManager
                .createQuery("delete from github.RateLimit r where resource = :resource and r.id != :currentLimitId")
                .setParameter("resource", resource)
                .setParameter("currentLimitId", currentLimit.getId())
                .executeUpdate();
    }

    private RateLimit getLimitByResource(String resource) {
        return rateLimitRepository.findFirstByResourceOrderByResetDesc(resource);
    }

}
