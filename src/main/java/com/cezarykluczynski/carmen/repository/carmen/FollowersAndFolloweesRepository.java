package com.cezarykluczynski.carmen.repository.carmen;

import com.cezarykluczynski.carmen.model.cassandra.carmen.FollowersAndFollowees;
import com.datastax.driver.mapping.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FollowersAndFolloweesRepository {

    private Mapper<FollowersAndFollowees> followersAndFolloweesMapper;

    private FollowersAndFolloweesAccessor followersAndFolloweesAccessor;

    @Autowired
    public FollowersAndFolloweesRepository(Mapper<FollowersAndFollowees> followersAndFolloweesMapper,
                             FollowersAndFolloweesAccessor followersAndFolloweesAccessor) {
        this.followersAndFolloweesMapper = followersAndFolloweesMapper;
        this.followersAndFolloweesAccessor = followersAndFolloweesAccessor;
    }

    public FollowersAndFollowees findByUserId(Long userId) {
        return followersAndFolloweesAccessor.findByUserId(userId);
    }

    public void save(FollowersAndFollowees followersAndFollowees) {
        followersAndFolloweesMapper.save(followersAndFollowees);
    }


}
