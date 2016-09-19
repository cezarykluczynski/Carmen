package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;

public interface UserFollowersRepositoryCustom {

    UserFollowers findOldestPropagationInDiscoverPhase();

    UserFollowers create(User userEntity, String phase);

}
