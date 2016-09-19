package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
public class UserFollowingRepositoryImpl extends AbstractPropagationRepositoryImpl<UserFollowing>
        implements UserFollowingRepositoryCustom {

    @Autowired
    private UserFollowingRepository userFollowingRepository;

    @Autowired
    public UserFollowingRepositoryImpl(EntityManager entityManager) {
        super(entityManager, UserFollowing.class);
    }

    @Override
    @Transactional
    public UserFollowing findOldestPropagationInDiscoverPhase() {
        return findOldestPropagationInPhase("discover");
    }

    @Override
    @Transactional
    public UserFollowing create(User userEntity, String phase) {
        UserFollowing userFollowingEntity = new UserFollowing();

        userFollowingEntity.setUser(userEntity);
        userFollowingEntity.setPhase(phase);
        userFollowingEntity.setUpdated(new Date());

        return userFollowingRepository.save(userFollowingEntity);
    }

}
