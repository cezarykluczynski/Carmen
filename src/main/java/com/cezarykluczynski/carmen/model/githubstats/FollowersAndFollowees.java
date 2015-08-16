package com.cezarykluczynski.carmen.model.githubstats;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table
public class FollowersAndFollowees {

    @PrimaryKey
    private Long id;

    public FollowersAndFollowees(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}