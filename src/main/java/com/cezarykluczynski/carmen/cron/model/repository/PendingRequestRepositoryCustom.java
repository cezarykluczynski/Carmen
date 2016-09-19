package com.cezarykluczynski.carmen.cron.model.repository;

import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.util.DateTimeConstants;

import java.util.HashMap;

public interface PendingRequestRepositoryCustom {

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

    PendingRequest findMostImportantPendingRequest();

    Long countByPropagationId(Long propagationId);

    PendingRequest postponeRequest(PendingRequest pendingRequest, DateTimeConstants milliseconds);

    boolean userEntityFollowersRequestIsBlocked(User userEntity);

}
