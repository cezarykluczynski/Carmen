package carmen.propagation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.HashMap;

import carmen.model.github.User;
import carmen.dao.github.UserDAOImpl;
import carmen.dao.propagations.UserFollowingDAOImpl;
import carmen.dao.apiqueue.PendingRequestDAOImpl;

@Component
public class UserFollowing implements Propagation {

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao;

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

        List<carmen.model.propagations.UserFollowing> userFollowingPropagations = propagationsUserFollowingDao.findByUser(userEntity);

        tryCreateDiscoverPhase(userFollowingPropagations);
    }

    private void tryCreateDiscoverPhase(List<carmen.model.propagations.UserFollowing> userFollowingPropagations) {
        if (userFollowingPropagations.isEmpty()) {
            createDiscoverPhase(userEntity);
            return;
        }

        for (carmen.model.propagations.UserFollowing propagation : userFollowingPropagations) {
            if (propagation.getPhase().equals("discover")) {
                return;
            }
        }

        createDiscoverPhase(userEntity);
    }

    private void createDiscoverPhase(User userEntity) {
        propagationsUserFollowingDao.create(userEntity, "discover");
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        pathParams.put("endpoint", "following_url");
        pathParams.put("user", userEntity.getLogin());
        apiqueuePendingRequestDao.create("UsersGhost", userEntity, pathParams, new HashMap<String, Object>(), 1);
    }

}
