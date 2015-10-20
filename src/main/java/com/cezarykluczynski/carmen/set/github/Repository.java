package com.cezarykluczynski.carmen.set.github;

import com.cezarykluczynski.carmen.util.github.GitHubResource;

import javax.persistence.Entity;

import java.util.Date;

@Entity
public class Repository implements GitHubResource {

    public Repository(
        Long id,
        Long parentId,
        String name,
        String fullName,
        String description,
        String homepage,
        boolean fork,
        String defaultBranch,
        String cloneUrl,
        Date created,
        Date pushed,
        Date updated
    ) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.homepage = homepage;
        this.fork = fork;
        this.defaultBranch = defaultBranch;
        this.cloneUrl = cloneUrl;
        this.created = created;
        this.pushed = pushed;
        this.updated = updated;
    }

    private Long id;

    private Long parentId;

    private String name;

    private String fullName;

    private String description;

    private String homepage;

    private boolean fork;

    private String defaultBranch;

    private String cloneUrl;

    private Date created;

    private Date updated;

    private Date pushed;

    public Long getId() {
        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getHomepage() {
        return homepage;
    }

    public boolean getFork() {
        return fork;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Date getPushed() {
        return pushed;
    }

    public Long getGitHubResourceId() {
        return getId();
    }

}
