package carmen.dao.apiqueue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.CriteriaSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import carmen.provider.github.GithubProvider;
import carmen.model.apiqueue.PendingRequest;
import carmen.model.github.User;
import carmen.exception.EmptyPendingRequestListException;

import java.util.List;
import java.util.HashMap;

@Repository
public class PendingRequestDAOImpl implements PendingRequestDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    GithubProvider githubProvider;

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
    @Transactional
    public PendingRequest create(
        String executor,
        User userEntity,
        HashMap<String, Object> pathParams,
        HashMap<String, Object> queryParams,
        Integer priority
    ) {
        PendingRequest pendingRequest = new PendingRequest();
        pendingRequest.setExecutor(executor);
        pendingRequest.setQueryParams(queryParams);
        pendingRequest.setPathParams(pathParams);
        pendingRequest.setPriority(priority);
        pendingRequest.setUser(userEntity);
        pendingRequest.setUpdated();

        Session session = sessionFactory.openSession();
        session.save(pendingRequest);
        session.flush();
        session.close();

        return pendingRequest;
    }

    @Override
    @Transactional(readOnly = true)
    public PendingRequest findMostImportantPendingRequest() throws EmptyPendingRequestListException {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(PendingRequest.class);
        criteria.addOrder(Order.asc("id"));
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

}
