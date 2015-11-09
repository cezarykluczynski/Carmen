package com.cezarykluczynski.carmen.propagation.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.HashMap;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;

@Component
public class UserFollowingPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    @Autowired
    UserDAO githubUserDAOImpl;

    @Autowired
    UserFollowingDAO propagationsUserFollowingDAOImpl;

    @Autowired
    PendingRequestDAO apiqueuePendingRequestDAOImpl;

    @Autowired
    PendingRequestFactory pendingRequestFactory;

    private User userEntity;

    @Override
    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public void propagate() {
        if (!userEntity.getFound()) {
            return;
        }

        List<com.cezarykluczynski.carmen.model.propagations.UserFollowing> userFollowingPropagations =
            propagationsUserFollowingDAOImpl.findByUser(userEntity);

        tryCreateDiscoverPhase(userFollowingPropagations);
    }

    private void tryCreateDiscoverPhase(
        List<com.cezarykluczynski.carmen.model.propagations.UserFollowing> userFollowingPropagations
    ) {
        if (userFollowingPropagations.isEmpty()) {
            createDiscoverPhase(userEntity);
        }
    }

    private void createDiscoverPhase(User userEntity) {
        Propagation propagationEntity = (Propagation) propagationsUserFollowingDAOImpl.create(userEntity, "discover");
        pendingRequestFactory.createPendingRequestForUserFollowingPropagation(propagationEntity);
    }

}
