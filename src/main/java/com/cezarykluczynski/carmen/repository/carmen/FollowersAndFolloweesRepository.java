package com.cezarykluczynski.carmen.repository.carmen;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.cassandra.repository.Query;
import com.cezarykluczynski.carmen.model.cassandra.carmen.FollowersAndFollowees;

public interface FollowersAndFolloweesRepository extends CrudRepository<FollowersAndFollowees, Long> {

    @Query("select * from followers_and_followees where user_id = ?0")
    FollowersAndFollowees findByUserId(Long userId);

}
