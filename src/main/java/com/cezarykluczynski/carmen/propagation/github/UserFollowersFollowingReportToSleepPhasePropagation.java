package com.cezarykluczynski.carmen.propagation.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.HashMap;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.repository.githubstats.FollowersAndFolloweesRepository;
import com.cezarykluczynski.carmen.model.githubstats.FollowersAndFollowees;
import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.model.propagations.UserFollowing;

@Component
public class UserFollowersFollowingReportToSleepPhasePropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDAOImpl;

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDAOImpl;

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDAOImpl;

    @Autowired
    FollowersAndFolloweesRepository followersAndFolloweesRepository;

    private User userEntity;

    FollowersAndFollowees followersAndFollowees;

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
        UserFollowers userFollowers = propagationsUserFollowersDAOImpl.findByUser(userEntity).get(0);
        UserFollowing userFollowing = propagationsUserFollowingDAOImpl.findByUser(userEntity).get(0);

        userFollowers.setPhase("sleep");
        userFollowing.setPhase("sleep");

        propagationsUserFollowersDAOImpl.update(userFollowers);
        propagationsUserFollowingDAOImpl.update(userFollowing);
    }

}
