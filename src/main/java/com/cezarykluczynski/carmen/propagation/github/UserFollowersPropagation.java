package com.cezarykluczynski.carmen.propagation.github;

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFollowersPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    private UserFollowersRepository userFollowersRepository;

    private PendingRequestFactory pendingRequestFactory;

    private User userEntity;

    @Autowired
    public UserFollowersPropagation(UserFollowersRepository userFollowersRepository,
                                    PendingRequestFactory pendingRequestFactory) {
        this.userFollowersRepository = userFollowersRepository;
        this.pendingRequestFactory = pendingRequestFactory;
    }

    @Override
    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public void propagate() {
        if (!userEntity.isFound()) {
            return;
        }

        tryCreateDiscoverPhase(userFollowersRepository.findOneByUser(userEntity));
    }

    private void tryCreateDiscoverPhase(UserFollowers propagationUserFollowersEntity) {
        if (propagationUserFollowersEntity == null) {
            createDiscoverPhase(userEntity);
        }
    }

    private void createDiscoverPhase(User baseUserEntity) {
        Propagation propagationUserFollowersEntity = userFollowersRepository.create(baseUserEntity, "discover");
        pendingRequestFactory.createPendingRequestForUserFollowersPropagation(propagationUserFollowersEntity);
    }

}
