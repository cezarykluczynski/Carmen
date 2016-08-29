package com.cezarykluczynski.carmen.repository.carmen;

import com.cezarykluczynski.carmen.model.cassandra.carmen.FollowersAndFollowees;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface FollowersAndFolloweesAccessor {

    @Query("select * from carmen.followers_and_followees where user_id = :user_id")
    FollowersAndFollowees findByUserId(@Param("user_id") Long userId);

}
