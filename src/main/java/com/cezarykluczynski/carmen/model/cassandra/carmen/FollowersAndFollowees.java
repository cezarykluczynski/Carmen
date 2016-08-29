package com.cezarykluczynski.carmen.model.cassandra.carmen;

import com.cezarykluczynski.carmen.model.CarmenNoSQLEntity;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(keyspace = "carmen", name = "followers_and_followees")
public class FollowersAndFollowees extends CarmenNoSQLEntity {

    @PartitionKey
    private UUID id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "followers_count")
    private int followersCount;

    @Column(name = "followees_count")
    private int followeesCount;

    @Column(name = "shared_count")
    private int sharedCount;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setId() {
        this.id = UUIDs.random();
    }

    public UUID getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFolloweesCount(int followeesCount) {
        this.followeesCount = followeesCount;
    }

    public int getFolloweesCount() {
        return followeesCount;
    }

    public void setSharedCount(int sharedCount) {
        this.sharedCount = sharedCount;
    }

    public int getSharedCount() {
        return sharedCount;
    }

}
