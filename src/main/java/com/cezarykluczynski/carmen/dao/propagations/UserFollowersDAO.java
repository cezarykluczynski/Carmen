package com.cezarykluczynski.carmen.dao.propagations;

import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.model.github.User;

import java.util.List;

public interface UserFollowersDAO {

    UserFollowers findByUser(User userEntity);

    UserFollowers findOldestPropagationInDiscoverPhase();

    UserFollowers create(User userEntity, String phase);

    void update(UserFollowers userFollowers);

    void delete(UserFollowers userFollowers);

    UserFollowers findById(Long id);

}
