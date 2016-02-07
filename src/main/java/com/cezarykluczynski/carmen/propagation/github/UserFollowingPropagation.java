package com.cezarykluczynski.carmen.propagation.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.model.propagations.UserFollowing;

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

        tryCreateDiscoverPhase(propagationsUserFollowingDAOImpl.findByUser(userEntity));
    }

    private void tryCreateDiscoverPhase(UserFollowing propagationUserFollowingEntity) {
        if (propagationUserFollowingEntity == null) {
            createDiscoverPhase(userEntity);
        }
    }

    private void createDiscoverPhase(User baseUserEntity) {
        Propagation propagationUserFollowingEntity = (Propagation)
                propagationsUserFollowingDAOImpl.create(baseUserEntity, "discover");
        pendingRequestFactory.createPendingRequestForUserFollowingPropagation(propagationUserFollowingEntity);
    }

}
