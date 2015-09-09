package com.cezarykluczynski.carmen.model.github;

import javax.persistence.*;

import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.model.propagations.UserFollowing;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.joda.time.MutableDateTime;

@Entity(name = "github.User")
@Table(schema = "github", name = "users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private com.cezarykluczynski.carmen.model.users.User user;

    @Column(name = "github_id")
    private Long githubId;

    @Column
    private String name;

    @Column
    private String login;

    @Column
    private Date updated;

    @Column
    private boolean found;

    @Column
    private boolean requested;

    @Column(name = "opt_out")
    private boolean optOut;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "github_type")
    private String type;

    @Column(name = "site_admin")
    private boolean siteAdmin;

    @Column
    private String company;

    @Column
    private String  blog;

    @Column(name = "github_location")
    private String  location;

    @Column
    private String  email;

    @Column
    private boolean hireable;

    @Column
    private String bio;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable
    (
        name="github.user_followers",
        joinColumns={ @JoinColumn(name="follower_id", referencedColumnName="id") },
        inverseJoinColumns={ @JoinColumn(name="followee_id", referencedColumnName="id") }
    )
    private Set<User> followers = new HashSet<User>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable
    (
        name="github.user_followers",
        joinColumns={ @JoinColumn(name="followee_id", referencedColumnName="id") },
        inverseJoinColumns={ @JoinColumn(name="follower_id", referencedColumnName="id") }
    )
    private Set<User> followees = new HashSet<User>();

    public void addFollower(User follower) {
        followers.add(follower);
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void addFollowee(User followee) {
        followees.add(followee);
    }

    public Set<User> getFollowees() {
        return followees;
    }

    @OneToOne(mappedBy="user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserFollowers userFollowers;

    public void setUserFollowers(UserFollowers userFollowers) {
        this.userFollowers = userFollowers;
    }

    public UserFollowers getUserFollowers() {
        return userFollowers;
    }

    @OneToOne(mappedBy="user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserFollowing userFollowing;

    public void setUserFollowing(UserFollowing userFollowing) {
        this.userFollowing = userFollowing;
    }

    public UserFollowing getUserFollowing() {
        return userFollowing;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public com.cezarykluczynski.carmen.model.users.User getUser() {
        return user;
    }

    public void setUser(com.cezarykluczynski.carmen.model.users.User user) {
        user = this.user;
    }

    public Long getGithubId() {
        return githubId;
    }

    public void setGithubId(Long githubId) {
        this.githubId = githubId;
    }

    public void setGithubId() {
        this.githubId = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setUpdated() {
        setUpdated(new Date());
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean getFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean getRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public boolean getOptOut() {
        return optOut;
    }

    public void setOptOut(boolean optOut) {
        this.optOut = optOut;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(boolean siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getHireable() {
        return hireable;
    }

    public void setHireable(boolean hireable) {
        this.hireable = hireable;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean canBeUpdated() {
        MutableDateTime yesterday = new MutableDateTime();
        yesterday.addDays(-1);
        Date date = yesterday.toDate();
        return date.after(updated);
    }
}
