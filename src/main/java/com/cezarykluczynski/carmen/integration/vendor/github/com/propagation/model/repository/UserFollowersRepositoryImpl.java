package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
public class UserFollowersRepositoryImpl extends AbstractPropagationRepositoryImpl<UserFollowers>
        implements UserFollowersRepositoryCustom {

    @Autowired
    private UserFollowersRepository userFollowersRepository;

    @Autowired
    public UserFollowersRepositoryImpl(EntityManager entityManager) {
        super(entityManager, UserFollowers.class);
    }

    @Override
    @Transactional
    public UserFollowers findOldestPropagationInDiscoverPhase() {
        return findOldestPropagationInPhase("discover");
    }

    @Override
    @Transactional
    public UserFollowers create(User userEntity, String phase) {
        UserFollowers userFollowersEntity = new UserFollowers();

        userFollowersEntity.setUser(userEntity);
        userFollowersEntity.setPhase(phase);
        userFollowersEntity.setUpdated(new Date());

        return userFollowersRepository.save(userFollowersEntity);
    }

}
