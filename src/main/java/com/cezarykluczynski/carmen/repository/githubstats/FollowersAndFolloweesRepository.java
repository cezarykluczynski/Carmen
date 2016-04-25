package com.cezarykluczynski.carmen.repository.githubstats;

import org.springframework.data.repository.CrudRepository;

import org.springframework.data.cassandra.repository.Query;

import com.cezarykluczynski.carmen.model.cassandra.github_social_stats.FollowersAndFollowees;

public interface FollowersAndFolloweesRepository extends CrudRepository<FollowersAndFollowees, Long> {

    @Query("select * from github_social_stats.followers_and_followees where user_id = ?0")
    FollowersAndFollowees findByUserId(Long userId);

}
