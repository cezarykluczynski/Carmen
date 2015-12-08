package com.cezarykluczynski.carmen.model.github;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import com.cezarykluczynski.carmen.util.github.GitHubResource;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.joda.time.MutableDateTime;

@Entity(name = "github.Repository")
@Table(schema = "github", name = "repositories")
public class Repository extends CarmenRelationalEntity implements GitHubResource {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name = "github_user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(optional=true)
    @JoinColumn(name="parent_id", nullable=true)
    private Repository parent;

    @Column(name = "github_id")
    private Long githubId;

    @Column
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String description;

    @Column
    private String homepage;

    @Column
    private boolean fork;

    @Column(name = "default_branch")
    private String defaultBranch;

    @Column(name = "clone_url")
    private String cloneUrl;

    @Column
    private Date created;

    @Column
    private Date updated;

    @Column
    private Date pushed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Repository getParent() {
        return parent;
    }

    public void setParent(Repository parent) {
        this.parent = parent;
    }

    public Long getGithubId() {
        return githubId;
    }

    public void setGithubId(Long githubId) {
        this.githubId = githubId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public boolean getFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getPushed() {
        return pushed;
    }

    public void setPushed(Date pushed) {
        this.pushed = pushed;
    }

    public Long getGitHubResourceId() {
        return getId();
    }

}
