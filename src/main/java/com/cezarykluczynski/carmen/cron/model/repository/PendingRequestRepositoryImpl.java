package com.cezarykluczynski.carmen.cron.model.repository;

import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.util.DateTimeConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

@Service
public class PendingRequestRepositoryImpl implements PendingRequestRepositoryCustom {

    @Autowired
    private PendingRequestRepository pendingRequestRepository;

    private EntityManager entityManager;

    @Autowired
    public PendingRequestRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public PendingRequest create(
            String executor,
            User userEntity,
            HashMap<String, Object> pathParams,
            HashMap<String, Object> queryParams,
            HashMap<String, Object> params,
            Object propagation,
            Integer priority
    ) {
        PendingRequest pendingRequest = new PendingRequest();
        pendingRequest.setExecutor(executor);
        pendingRequest.setQueryParams(queryParams);
        pendingRequest.setPathParams(pathParams);
        pendingRequest.setParams(params);
        pendingRequest.setPriority(priority);
        pendingRequest.setUser(userEntity);
        pendingRequest.setUpdated(new Date());

        if (propagation instanceof Propagation) {
            pendingRequest.setPropagationId(((Propagation) propagation).getId());
        }

        return create(pendingRequest);
    }

    @Override
    @Transactional
    public PendingRequest create(PendingRequest pendingRequest) {
        return pendingRequestRepository.save(pendingRequest);
    }

    @Override
    public PendingRequest create(
            String executor,
            User userEntity,
            HashMap<String, Object> pathParams,
            HashMap<String, Object> queryParams,
            HashMap<String, Object> params,
            Integer priority
    ) {
        return create(executor, userEntity, pathParams, queryParams, params, null, priority);
    }

    @Override
    @Transactional
    public PendingRequest findMostImportantPendingRequest() {
        return pendingRequestRepository.findFirstByOrderByPriorityDesc();
    }

    @Transactional
    public void update(PendingRequest pendingRequest) {
        pendingRequestRepository.save(pendingRequest);
    }

    @Transactional
    public void delete(PendingRequest pendingRequest) {
        pendingRequestRepository.delete(pendingRequest);
    }

    @Override
    @Transactional
    public Long countByPropagationId(Long propagationId) {
        // TODO: move to PendingRequestRepository
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(PendingRequest.class);
        criteria.add(Restrictions.eq("propagationId", propagationId));
        criteria.setProjection(Projections.rowCount());
        Long result = (Long) criteria.uniqueResult();
        return result;
    }

    @Override
    @Transactional
    public PendingRequest postponeRequest(PendingRequest pendingRequest, DateTimeConstants milliseconds) {
        Date updated = pendingRequest.getUpdated();
        Date updatedPostponed = new Date();
        updatedPostponed.setTime(updated.getTime() + milliseconds.getValue());
        pendingRequest.setUpdated(updatedPostponed);
        update(pendingRequest);
        return pendingRequest;
    }

    @Override
    @Transactional
    public boolean userEntityFollowersRequestIsBlocked(User userEntity) {
        boolean blocked = ((BigInteger) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM api_queue.pending_requests pr " +
                        "LEFT JOIN github.user_followers uf ON uf.follower_id = pr.github_user_id " +
                        "WHERE uf.followee_id = :userId AND pr.executor = :executor"
        )
                .setParameter("userId", userEntity.getId())
                .setParameter("executor", "UsersGhostPaginator")
                .getSingleResult()).intValue() > 0;
        return blocked;
    }

}
