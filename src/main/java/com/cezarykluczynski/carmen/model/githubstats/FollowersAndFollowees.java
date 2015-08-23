package com.cezarykluczynski.carmen.model.githubstats;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.datastax.driver.core.utils.UUIDs;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;

import java.math.BigDecimal;

@Table(value = "followers_and_followees")
public class FollowersAndFollowees {

    @PrimaryKey
    private UUID id;

    @Column(value = "user_id")
    private int userId;

    @Column(value = "followers_count")
    private int followersCount;

    @Column(value = "followees_count")
    private int followeesCount;

    @Column(value = "shared_count")
    private int sharedCount;

   //  public FollowersAndFollowees(int userId, int followersCount, int followeesCount, int sharedCount) {
   //      this.userId = userId;
   //      this.followersCount = followersCount;
   //      this.followeesCount = followeesCount;
   //      this.sharedCount = sharedCount;
   //  }

   // public FollowersAndFollowees(Long userId, int followersCount, int followeesCount, int sharedCount) {
   //     this.id = UUIDs.random();
   //     this.userId = new BigDecimal(userId).intValueExact();
   //     this.followersCount = followersCount;
   //     this.followeesCount = followeesCount;
   //     this.sharedCount = sharedCount;
   // }

    // public FollowersAndFollowees(Long userId) {
    //     setUserId(userId);
    //     setId();
    // }

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
