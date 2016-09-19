package com.cezarykluczynski.carmen.propagation.github;

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFollowingPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    private UserFollowingRepository userFollowingRepository;

    private PendingRequestFactory pendingRequestFactory;

    private User userEntity;

    @Autowired
    public UserFollowingPropagation(UserFollowingRepository userFollowingRepository,
                                    PendingRequestFactory pendingRequestFactory) {
        this.userFollowingRepository = userFollowingRepository;
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

        tryCreateDiscoverPhase(userFollowingRepository.findOneByUser(userEntity));
    }

    private void tryCreateDiscoverPhase(UserFollowing propagationUserFollowingEntity) {
        if (propagationUserFollowingEntity == null) {
            createDiscoverPhase(userEntity);
        }
    }

    private void createDiscoverPhase(User baseUserEntity) {
        Propagation propagationUserFollowingEntity = userFollowingRepository.create(baseUserEntity, "discover");
        pendingRequestFactory.createPendingRequestForUserFollowingPropagation(propagationUserFollowingEntity);
    }

}
