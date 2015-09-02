package com.cezarykluczynski.carmen.dao.propagations;

import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.model.github.User;

import java.util.List;

public interface UserFollowersDAO {

    public List<UserFollowers> findByUser(User userEntity);

    public UserFollowers findOldestPropagationInDiscoverPhase();

    public UserFollowers create(User userEntity, String phase);

    public void update(UserFollowers userFollowers);

    public void delete(UserFollowers userFollowers);

    public UserFollowers findById(Long id);

}
