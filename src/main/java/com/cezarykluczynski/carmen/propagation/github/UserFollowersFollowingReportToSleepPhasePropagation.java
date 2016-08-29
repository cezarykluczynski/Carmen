package com.cezarykluczynski.carmen.propagation.github;

import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO;
import com.cezarykluczynski.carmen.model.cassandra.carmen.FollowersAndFollowees;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.model.propagations.UserFollowing;
import com.cezarykluczynski.carmen.repository.carmen.FollowersAndFolloweesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO make thread-safe
@Component
public class UserFollowersFollowingReportToSleepPhasePropagation implements
        com.cezarykluczynski.carmen.propagation.Propagation {

    private UserDAO githubUserDAOImpl;

    private UserFollowersDAO propagationsUserFollowersDAOImpl;

    private UserFollowingDAO propagationsUserFollowingDAOImpl;

    private FollowersAndFolloweesRepository followersAndFolloweesRepository;

    private User userEntity;

    private FollowersAndFollowees followersAndFollowees;

    @Autowired
    public UserFollowersFollowingReportToSleepPhasePropagation(UserDAO githubUserDAOImpl,
                                                   UserFollowersDAO propagationsUserFollowersDAOImpl,
                                                   UserFollowingDAO propagationsUserFollowingDAOImpl,
                                                   FollowersAndFolloweesRepository followersAndFolloweesRepository) {
        this.githubUserDAOImpl = githubUserDAOImpl;
        this.propagationsUserFollowersDAOImpl = propagationsUserFollowersDAOImpl;
        this.propagationsUserFollowingDAOImpl = propagationsUserFollowingDAOImpl;
        this.followersAndFolloweesRepository = followersAndFolloweesRepository;
    }

    @Override
    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public void propagate() {
        setFollowersAndFollowingFromUserEntity();
        hydrateFollowersAndFollowees();
        saveFollowersAndFollowees();
        moveFollowersFollowingPropagationToSleepPhase();
    }

    private void setFollowersAndFollowingFromUserEntity() {
        followersAndFollowees = followersAndFolloweesRepository.findByUserId(userEntity.getId());

        if (followersAndFollowees == null) {
            followersAndFollowees = new FollowersAndFollowees();
            followersAndFollowees.setId();
            followersAndFollowees.setUserId(userEntity.getId());
        }
    }

    private void hydrateFollowersAndFollowees() {
        int followersCount = githubUserDAOImpl.countFollowers(userEntity);
        int followeesCount = githubUserDAOImpl.countFollowees(userEntity);
        int followersFolloweesCount = githubUserDAOImpl.countFollowersFollowing(userEntity);

        followersAndFollowees.setFollowersCount(followersCount);
        followersAndFollowees.setFolloweesCount(followeesCount);
        followersAndFollowees.setSharedCount(followersFolloweesCount);
    }

    private void saveFollowersAndFollowees() {
        followersAndFolloweesRepository.save(followersAndFollowees);
    }

    private void moveFollowersFollowingPropagationToSleepPhase() {
        UserFollowers userFollowers = propagationsUserFollowersDAOImpl.findByUser(userEntity);
        UserFollowing userFollowing = propagationsUserFollowingDAOImpl.findByUser(userEntity);

        userFollowers.setPhase("sleep");
        userFollowing.setPhase("sleep");

        propagationsUserFollowersDAOImpl.update(userFollowers);
        propagationsUserFollowingDAOImpl.update(userFollowing);
    }

}
