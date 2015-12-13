package com.cezarykluczynski.carmen.dao.propagations;

import com.cezarykluczynski.carmen.model.propagations.UserFollowing;
import com.cezarykluczynski.carmen.model.github.User;

import java.util.List;

public interface UserFollowingDAO {

    UserFollowing findByUser(User userEntity);

    UserFollowing findOldestPropagationInDiscoverPhase();

    UserFollowing create(User userEntity, String phase);

    void update(UserFollowing userFollowing);

    void delete(UserFollowing userFollowing);

    UserFollowing findById(Long id);

}
