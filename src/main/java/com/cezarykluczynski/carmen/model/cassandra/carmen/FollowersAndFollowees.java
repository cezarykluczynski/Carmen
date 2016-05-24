package com.cezarykluczynski.carmen.model.cassandra.carmen;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.datastax.driver.core.utils.UUIDs;

import java.util.UUID;

import com.cezarykluczynski.carmen.model.CarmenNoSQLEntity;

import org.springframework.data.cassandra.mapping.Column;

import java.math.BigDecimal;

@Table("followers_and_followees")
public class FollowersAndFollowees extends CarmenNoSQLEntity {

    @PrimaryKey
    private UUID id;

    @Column("user_id")
    private int userId;

    @Column("followers_count")
    private int followersCount;

    @Column("followees_count")
    private int followeesCount;

    @Column("shared_count")
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
        setUserId(new BigDecimal(userId).intValueExact());
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
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
