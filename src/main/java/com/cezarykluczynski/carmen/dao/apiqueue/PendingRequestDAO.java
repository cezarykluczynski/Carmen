package com.cezarykluczynski.carmen.dao.apiqueue;

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.exception.EmptyPendingRequestListException;
import com.cezarykluczynski.carmen.util.DateTimeConstants;

import java.util.HashMap;
import java.util.List;

public interface PendingRequestDAO {

    List<PendingRequest> findByUser(User userEntity);

    PendingRequest create(
        String executor,
        User userEntity,
        HashMap<String, Object> pathParams,
        HashMap<String, Object> queryParams,
        HashMap<String, Object> params,
        Object propagation,
        Integer priotity
    );

    PendingRequest create(
        String executor,
        User userEntity,
        HashMap<String, Object> pathParams,
        HashMap<String, Object> queryParams,
        HashMap<String, Object> params,
        Integer priotity
    );

    PendingRequest create(PendingRequest pendingRequest);

    PendingRequest findMostImportantPendingRequest() throws EmptyPendingRequestListException;

    void update(PendingRequest pendingRequest);

    void delete(PendingRequest pendingRequest);

    Long countByPropagationId(Long propagationId);

    PendingRequest findById(Long pendingRequestId);

    PendingRequest postponeRequest(PendingRequest pendingRequest, DateTimeConstants milliseconds);

    boolean userEntityFollowersRequestIsBlocked(User userEntity);

}
