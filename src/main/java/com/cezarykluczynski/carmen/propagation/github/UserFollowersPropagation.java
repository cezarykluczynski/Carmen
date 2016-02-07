package com.cezarykluczynski.carmen.propagation.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.model.propagations.UserFollowers;

@Component
public class UserFollowersPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    @Autowired
    UserDAO githubUserDAOImpl;

    @Autowired
    UserFollowersDAO propagationsUserFollowersDAOImpl;

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDAOImpl;

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

        tryCreateDiscoverPhase(propagationsUserFollowersDAOImpl.findByUser(userEntity));
    }

    private void tryCreateDiscoverPhase(UserFollowers propagationUserFollowersEntity) {
        if (propagationUserFollowersEntity == null) {
            createDiscoverPhase(userEntity);
        }
    }

    private void createDiscoverPhase(User baseUserEntity) {
        Propagation propagationUserFollowersEntity = (Propagation)
                propagationsUserFollowersDAOImpl.create(baseUserEntity, "discover");
        pendingRequestFactory.createPendingRequestForUserFollowersPropagation(propagationUserFollowersEntity);
    }

}
