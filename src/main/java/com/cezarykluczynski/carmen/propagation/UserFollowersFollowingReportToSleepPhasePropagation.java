package com.cezarykluczynski.carmen.propagation;

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
    UserFollowersDAOImpl propagationsUserFollowersDao;

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao;

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

    @Autowired
    FollowersAndFolloweesRepository followersAndFolloweesRepository;

    private User userEntity;

    FollowersAndFollowees followersAndFollowees;

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public void propagate() {
        setFollowersAndFollowingFromUserEntity();
        hydrateFollowersAndFollowees();
        saveFollowersAndFollowees();
        moveFollowersFollowingPropagationToSleepPhase();
    }

    private void setFollowersAndFollowingFromUserEntity() {
        followersAndFollowees = null;

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
        UserFollowers userFollowers = propagationsUserFollowersDao.findByUser(userEntity).get(0);
        UserFollowing userFollowing = propagationsUserFollowingDao.findByUser(userEntity).get(0);

        userFollowers.setPhase("sleep");
        userFollowing.setPhase("sleep");

        propagationsUserFollowersDao.update(userFollowers);
        propagationsUserFollowingDao.update(userFollowing);
    }

}
