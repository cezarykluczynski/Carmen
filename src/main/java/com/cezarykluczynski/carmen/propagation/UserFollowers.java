package com.cezarykluczynski.carmen.propagation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.HashMap;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl;

@Component
public class UserFollowers implements Propagation {

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDao;

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

    private User userEntity;

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public void propagate() {
        if (!userEntity.getFound()) {
            return;
        }

        List<com.cezarykluczynski.carmen.model.propagations.UserFollowers> userFollowersPropagations = propagationsUserFollowersDao.findByUser(userEntity);

        tryCreateDiscoverPhase(userFollowersPropagations);
    }

    private void tryCreateDiscoverPhase(List<com.cezarykluczynski.carmen.model.propagations.UserFollowers> userFollowersPropagations) {
        if (userFollowersPropagations.isEmpty()) {
            createDiscoverPhase(userEntity);
            return;
        }

        for (com.cezarykluczynski.carmen.model.propagations.UserFollowers propagation : userFollowersPropagations) {
            if (propagation.getPhase().equals("discover")) {
                return;
            }
        }

        createDiscoverPhase(userEntity);
    }

    private void createDiscoverPhase(User userEntity) {
        propagationsUserFollowersDao.create(userEntity, "discover");
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        pathParams.put("endpoint", "followers_url");
        pathParams.put("login", userEntity.getLogin());
        apiqueuePendingRequestDao.create(
            "UsersGhostPaginator",
            userEntity,
            pathParams,
            new HashMap<String, Object>(),
            new HashMap<String, Object>(),
            1
        );
    }

}
