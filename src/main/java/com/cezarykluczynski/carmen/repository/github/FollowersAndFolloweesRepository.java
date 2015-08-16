package com.cezarykluczynski.carmen.repository.github;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.cassandra.repository.Query;

import com.cezarykluczynski.carmen.model.githubstats.FollowersAndFollowees;

@EnableJpaRepositories
public interface FollowersAndFolloweesRepository extends CrudRepository<FollowersAndFollowees, Long> {

    @Query("select * from github_stats.followers_and_followees where id = :id")
    FollowersAndFollowees findById(Long id);

}