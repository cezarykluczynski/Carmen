package com.cezarykluczynski.carmen.propagation.github;

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository;
import com.cezarykluczynski.carmen.model.cassandra.carmen.FollowersAndFollowees;
import com.cezarykluczynski.carmen.repository.carmen.FollowersAndFolloweesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO make thread-safe
@Component
public class UserFollowersFollowingReportToSleepPhasePropagation implements
        com.cezarykluczynski.carmen.propagation.Propagation {

    private UserRepository userRepository;

    private UserFollowersRepository userFollowersRepository;

    private UserFollowingRepository userFollowingRepository;

    private FollowersAndFolloweesRepository followersAndFolloweesRepository;

    private User userEntity;

    private FollowersAndFollowees followersAndFollowees;

    @Autowired
    public UserFollowersFollowingReportToSleepPhasePropagation(UserRepository userRepository,
            UserFollowersRepository userFollowersRepository, UserFollowingRepository userFollowingRepository,
            FollowersAndFolloweesRepository followersAndFolloweesRepository) {
        this.userRepository = userRepository;
        this.userFollowersRepository = userFollowersRepository;
        this.userFollowingRepository = userFollowingRepository;
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
        int followersCount = userRepository.countFollowers(userEntity);
        int followeesCount = userRepository.countFollowees(userEntity);
        int followersFolloweesCount = userRepository.countFollowersFollowing(userEntity);

        followersAndFollowees.setFollowersCount(followersCount);
        followersAndFollowees.setFolloweesCount(followeesCount);
        followersAndFollowees.setSharedCount(followersFolloweesCount);
    }

    private void saveFollowersAndFollowees() {
        followersAndFolloweesRepository.save(followersAndFollowees);
    }

    private void moveFollowersFollowingPropagationToSleepPhase() {
        UserFollowers userFollowers = userFollowersRepository.findOneByUser(userEntity);
        UserFollowing userFollowing = userFollowingRepository.findOneByUser(userEntity);

        userFollowers.setPhase("sleep");
        userFollowing.setPhase("sleep");

        userFollowersRepository.save(userFollowers);
        userFollowingRepository.save(userFollowing);
    }

}
