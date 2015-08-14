package com.cezarykluczynski.carmen.dao.propagations;

import com.cezarykluczynski.carmen.model.propagations.UserFollowing;
import com.cezarykluczynski.carmen.model.github.User;

import java.util.List;

public interface UserFollowingDAO {

    public List<UserFollowing> findByUser(User userEntity);

    public UserFollowing create(User userEntity, String phase);

    public void update(UserFollowing userFollowing);

    public UserFollowing findById(Long id);

}
