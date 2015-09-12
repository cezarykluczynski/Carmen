package com.cezarykluczynski.carmen.dao.apiqueue;

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.exception.EmptyPendingRequestListException;

import java.util.HashMap;
import java.util.List;

public interface PendingRequestDAO {

    public List<PendingRequest> findByUser(User userEntity);

    public PendingRequest create(
        String executor,
        User userEntity,
        HashMap<String, Object> pathParams,
        HashMap<String, Object> queryParams,
        HashMap<String, Object> params,
        Object propagation,
        Integer priotity
    );

    public PendingRequest create(
        String executor,
        User userEntity,
        HashMap<String, Object> pathParams,
        HashMap<String, Object> queryParams,
        HashMap<String, Object> params,
        Integer priotity
    );

    public PendingRequest create(PendingRequest pendingRequest);

    public PendingRequest findMostImportantPendingRequest() throws EmptyPendingRequestListException;

    public void update(PendingRequest pendingRequest);

    public void delete(PendingRequest pendingRequest);

    public Long countByPropagationId(Long propagationId);

}
