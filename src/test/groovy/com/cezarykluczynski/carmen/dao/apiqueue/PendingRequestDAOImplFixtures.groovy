package com.cezarykluczynski.carmen.dao.apiqueue

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl

import java.util.HashMap

@Component
class PendingRequestDAOImplFixtures {

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao

    public PendingRequest createPendingRequestEntityUsingUserEntity(User userEntity) {
        HashMap<String, Object> emptyParams = new HashMap<String, Object>();
        PendingRequest pendingRequestEntity = apiqueuePendingRequestDao.create(
            "RandomExecutor",
            userEntity,
            emptyParams,
            emptyParams,
            emptyParams,
            0
        )

        return pendingRequestEntity
    }

    public void deletePendingRequestEntity(PendingRequest pendingRequestEntity) {
        apiqueuePendingRequestDao.delete pendingRequestEntity
    }

}
