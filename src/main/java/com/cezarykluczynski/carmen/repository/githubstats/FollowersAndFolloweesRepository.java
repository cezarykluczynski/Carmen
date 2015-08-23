package com.cezarykluczynski.carmen.repository.githubstats;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.cassandra.repository.Query;

import com.cezarykluczynski.carmen.model.githubstats.FollowersAndFollowees;

@EnableJpaRepositories
public interface FollowersAndFolloweesRepository extends CrudRepository<FollowersAndFollowees, Long> {

    @Query("select * from github_stats.followers_and_followees where user_id = ?0")
    FollowersAndFollowees findByUserId(Long userId);

}