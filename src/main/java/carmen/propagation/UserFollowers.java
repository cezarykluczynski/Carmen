package carmen.propagation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import carmen.model.github.User;
import carmen.dao.github.UserDAOImpl;
import carmen.dao.propagations.UserFollowersDAOImpl;

@Component
public class UserFollowers implements Propagation {

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDao;

    private User userEntity;

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public void propagate() {
        if (!userEntity.getFound()) {
            return;
        }

        List<carmen.model.propagations.UserFollowers> userFollowersPropagations = propagationsUserFollowersDao.findByUser(userEntity);

        tryCreateDiscoverPhase(userFollowersPropagations);
    }

    private void tryCreateDiscoverPhase(List<carmen.model.propagations.UserFollowers> userFollowersPropagations) {
        if (userFollowersPropagations.isEmpty()) {
            createDiscoverPhase(userEntity);
            return;
        }

        for (carmen.model.propagations.UserFollowers propagation : userFollowersPropagations) {
            if (propagation.getPhase().equals("discover")) {
                return;
            }
        }

        createDiscoverPhase(userEntity);
    }

    private void createDiscoverPhase(User userEntity) {
        propagationsUserFollowersDao.create(userEntity, "discover");
    }

}