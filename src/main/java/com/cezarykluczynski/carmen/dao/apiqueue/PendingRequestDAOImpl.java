package com.cezarykluczynski.carmen.dao.apiqueue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cezarykluczynski.carmen.client.github.GithubClient;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.exception.EmptyPendingRequestListException;
import com.cezarykluczynski.carmen.util.DateTimeConstants;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

@Repository
public class PendingRequestDAOImpl implements PendingRequestDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    GithubClient githubClient;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PendingRequest> findByUser(User userEntity) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(PendingRequest.class);
        criteria.add(Expression.eq("user", userEntity));
        List<PendingRequest> list = criteria.list();
        session.close();
        return list;
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
        pendingRequest.setUpdated();

        if (propagation instanceof Propagation) {
            pendingRequest.setPropagation((Propagation) propagation);
        }

        return create(pendingRequest);
    }

    @Override
    @Transactional
    public PendingRequest create(PendingRequest pendingRequest) {
        Session session = sessionFactory.openSession();
        session.save(pendingRequest);
        session.flush();
        session.close();

        return pendingRequest;
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
    @Transactional(readOnly = true)
    public PendingRequest findMostImportantPendingRequest() throws EmptyPendingRequestListException {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(PendingRequest.class);
        criteria.addOrder(Order.desc("priority"));
        criteria.setMaxResults(1);

        List<PendingRequest> list = criteria.list();
        session.close();

        if (list.size() == 0) {
            throw new EmptyPendingRequestListException();
        }

        return list.get(0);
    }

    @Override
    @Transactional
    public void update(PendingRequest pendingRequest) {
        Session session = sessionFactory.openSession();
        session.update(pendingRequest);
        session.flush();
        session.close();
    }

    @Override
    @Transactional
    public void delete(PendingRequest pendingRequest) {
        Session session = sessionFactory.openSession();
        session.delete(pendingRequest);
        session.flush();
        session.close();
    }

    @Override
    @Transactional
    public Long countByPropagationId(Long propagationId) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(PendingRequest.class);
        criteria.add(Restrictions.eq("propagationId", propagationId));
        criteria.setProjection(Projections.rowCount());
        Long result = (Long) criteria.uniqueResult();
        session.close();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public PendingRequest findById(Long pendingRequestId) {
        Session session = sessionFactory.openSession();
        PendingRequest pendingRequest = (PendingRequest) session.get(PendingRequest.class, pendingRequestId);
        session.close();
        return pendingRequest;
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
        Session session = sessionFactory.openSession();

        boolean blocked = ((BigInteger) session.createSQLQuery(
            "SELECT COUNT(*) FROM api_queue.pending_requests pr " +
            "LEFT JOIN github.user_followers uf ON uf.follower_id = pr.github_user_id " +
            "WHERE uf.followee_id = :userId AND pr.executor = :executor"
        )
            .setParameter("userId", userEntity.getId())
            .setParameter("executor", "UsersGhostPaginator")
            .uniqueResult()).intValue() > 0;
        session.close();
        return blocked;
    }

}
