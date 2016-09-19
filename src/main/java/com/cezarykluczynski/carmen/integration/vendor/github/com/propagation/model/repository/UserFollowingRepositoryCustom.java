package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;

public interface UserFollowingRepositoryCustom {

    UserFollowing findOldestPropagationInDiscoverPhase();

    UserFollowing create(User userEntity, String phase);

}
